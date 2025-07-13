package com.walmart.voiceconcierge

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import com.walmart.voiceconcierge.databinding.ActivityMainBinding
import com.walmart.voiceconcierge.db.AppDatabase
import com.walmart.voiceconcierge.db.SearchHistory
import com.walmart.voiceconcierge.ui.ProductAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding
    private lateinit var db: AppDatabase
    private var stateCode: String? = null

    private val MIC_REQUEST_CODE = 100

    private val voiceLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
        val recognizedText = matches?.firstOrNull()

        if (recognizedText.isNullOrBlank()) {
            Toast.makeText(this, "Sorry, I couldn't hear you. Try again.", Toast.LENGTH_SHORT).show()
        } else {
            b.etInput.setText(recognizedText)
            triggerSearch(recognizedText)
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION])
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        db = AppDatabase.get(applicationContext)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECORD_AUDIO),
            0
        )

        LocationServices.getFusedLocationProviderClient(this)
            .lastLocation
            .addOnSuccessListener { loc: Location? ->
                loc?.let { resolveState(it) }
            }

        b.recyclerView.layoutManager = LinearLayoutManager(this)

        b.btnSearch.setOnClickListener {
            val query = b.etInput.text.toString().trim()
            if (query.isNotBlank()) {
                triggerSearch(query)
            }
        }

        b.btnVoice.setOnClickListener {
            startVoiceInput()
        }
    }

    private fun startVoiceInput() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                MIC_REQUEST_CODE
            )
        } else {
            launchVoiceRecognizer()
        }
    }

    private fun launchVoiceRecognizer() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "What are you looking for?")
        }
        voiceLauncher.launch(intent)
    }

    private fun resolveState(loc: Location) {
        val geocoder = Geocoder(this, Locale.getDefault())
        val address = geocoder.getFromLocation(loc.latitude, loc.longitude, 1)?.firstOrNull()
        stateCode = address?.adminArea
    }

    private fun triggerSearch(query: String) {
        b.shimmer.startShimmer()
        b.recyclerView.adapter = null

        lifecycleScope.launch(Dispatchers.IO) {
            db.searchHistoryDao().insert(SearchHistory(query = query))

            val intent = IntentParser.parse(query)

            val feedback = db.feedbackDao().getAllScores()
                .associate { it.productId to it.score }

            val statePrefs = stateCode?.let {
                db.stateLogDao().getScoresByState(it)
            }?.associate { it.productId to it.score } ?: emptyMap()

            val products = ProductRepository.recommend(intent, feedback, statePrefs)

            withContext(Dispatchers.Main) {
                b.shimmer.stopShimmer()
                b.recyclerView.adapter = ProductAdapter(products, db, stateCode)
            }
        }
    }
}

