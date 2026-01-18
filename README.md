#  VoyageMap

VoyageMap is an Android travel application that allows users to **search cities**, **discover popular places**, and **explore tourist attractions** using real-world geographic APIs.  
The app is built with **modern Android architecture (MVVM)** and supports **online and offline data**, as well as **user authentication**.

---

##  Features

-  **Search only real cities**
-  **City details** with description
-  **Popular places & attractions** near the city
-  Ranking of main tourist places
- ️ Location-based search using coordinates
-  User authentication (Firebase)
-  Local caching for offline access
- ️ Remote database for user data & favorites

---

##  Architecture

The application follows **MVVM (Model–View–ViewModel)** architecture:

UI (Jetpack Compose)
->
ViewModel
->
Repository
->
Local Database (Room) + Remote APIs / Firebase


This architecture ensures:
- Clear separation of concerns
- Easy testing & maintenance
- Scalability

---

##  Technologies Used

### Android
- Kotlin
- Jetpack Compose
- ViewModel
- StateFlow
- Retrofit
- Gson

### APIs
- **GeoDB Cities API** – search only real cities
- **Geoapify Geocoding API** – convert city names to coordinates
- **Geoapify Places API** – retrieve tourist attractions
- **Wikipedia REST API** – city descriptions and additional info

### Databases
- **Room (SQLite)** – local database for offline usage
- **Firebase Authentication** – user login & registration
- **Firebase Firestore** – user profiles and favorites

---

##  How the App Works

1. User searches for a city  
   → GeoDB Cities API returns **only cities**

2. User selects a city  
   → Geoapify Geocoding API provides **latitude & longitude**

3. App fetches nearby places  
   → Geoapify Places API returns **tourist attractions**

4. Results are ranked  
   → Main places (attractions, museums, heritage) appear first

5. Data is cached locally  
   → Works even when offline

---

##  Permissions Used

- Internet access
- Location access (optional, for future features)

---

---

##  Setup Instructions

1. Clone the repository
2. Open the project in **Android Studio**
3. Add your API keys:
    - GeoDB Cities API
    - Geoapify API
    - Firebase configuration (`google-services.json`)
4. Sync Gradle
5. Run the app on an emulator or physical device

---

##  Future Improvements

-  Map view with markers
-  User favorites sync across devices
-  Navigation routes
-  Images for places

---

## License

This project is for educational purposes.

---

Enjoy exploring the world with **VoyageMap** 🌎✨

Made With Love From 
- Godefroy Lecluse
- Gaurav Mittal
