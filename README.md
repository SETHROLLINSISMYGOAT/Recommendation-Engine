# 🛒 Walmart Voice Concierge (Hackathon Prototype)

A smart shopping assistant that allows users to **search for Walmart products** by **voice or text**, and receive **personalized recommendations** based on **price, location, feedback**, and **popularity**.

Built for **Walmart Hackathon 2025** – this prototype simulates a full shopping experience with **offline mock data**, **feedback tracking**, and **smart recommendations**.

---

## 🔥 Key Features

### 🔍 Smart Voice/Text Search
- Speak or type natural queries like:
  - `"Find chocolate under ₹150"`
  - `"Show dairy popular in Punjab"`
- Powered by custom `IntentParser` (NLP-style logic)

### ⚡ Real-Time UI
- Shimmer loading animation while fetching results
- Clean product card UI (image, title, aisle, price, popularity)

### ❤️ Feedback Engine
- 👍 **Like** / 👎 **Dislike** per product
- 🛒 **Add to Cart** with stored state
- All actions saved to **Room DB** for personalization

### 📍 Location-Aware Recommendations
- Uses **GPS** to fetch state and recommend products popular in that region
- Adjusts score based on `StateLog` entries

### 🧠 Recommendation Ranking
Products sorted based on:
- Feedback (`Like/Dislike`)
- Cart count
- Likes from mock data
- Regional preferences

### 🗃 Persistent Data (Room DB)
- **SearchHistory**
- **Feedback**
- **Cart**
- **State Preferences**

### 🛍️ Cart Management
- View all added items
- ✅ "Change Qty"
- 🗑 "Remove Item"
- 📊 Total amount shown

### 🧾 Liked Products Screen *(Optional Add-on)*
- View all liked products (based on feedback DB)
  

---

## 🚀 Tech Stack

- **Kotlin (Android Jetpack)**
- **Room DB**
- **Google Location API**
- **Speech Recognizer API**
- **Coroutines**
- **Shimmer Layout**
- **Glide** (images)
- **Material Components**




