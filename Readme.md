# NumberBook — DialVault Edition

![Android](https://img.shields.io/badge/Android-Java-3DDC84?style=flat&logo=android)
![PHP](https://img.shields.io/badge/Backend-PHP-777BB4?style=flat&logo=php)
![MySQL](https://img.shields.io/badge/Database-MySQL-4479A1?style=flat&logo=mysql)
![Retrofit](https://img.shields.io/badge/Network-Retrofit2-FF6B35?style=flat)

> A connected Android application that reads phone contacts, syncs them to a remote MySQL database via a PHP REST API, and performs smart statistical search by name or number.

---

## Project Overview

NumberBook reads contacts stored on the device, displays them in a styled RecyclerView, synchronizes them to a remote server, and allows users to search the database with percentage-based name statistics per phone number.

---

## Architecture

```
Android App (Java)
      │
      │  HTTP (Retrofit2 + Gson)
      ▼
PHP REST API (dialvault-api)
      │
      │  PDO
      ▼
MySQL Database (ring_vault)
```

---

## Package Structure

```
com.example.numberbook/
│
├── MainActivity.java          — Main screen, permission handling, load/sync/search
├── RingEntry.java             — Contact model with @SerializedName for Gson
├── VaultResponse.java         — API insert response model (success + message)
├── SearchResult.java          — Search result model with occurrence + percentage
├── DialVaultApi.java          — Retrofit interface defining all API endpoints
├── VaultNetworkClient.java    — Retrofit singleton with base URL
├── DialRingAdapter.java       — RecyclerView adapter for contact list
└── SearchResultAdapter.java   — RecyclerView adapter for search results with stats
```

```
res/layout/
│
├── activity_main.xml          — Main screen layout (buttons, search field, RecyclerView)
├── row_ring_entry.xml         — Single contact row layout
└── row_search_result.xml      — Search result row with percentage badge
```

```
dialvault-api/
│
├── config/
│   └── VaultBridge.php        — PDO database connection
├── model/
│   └── RingEntry.php          — Server-side contact blueprint
├── service/
│   └── DialVaultService.php   — All SQL logic (insert, getAll, search with stats)
└── api/
    ├── storeEntry.php         — POST endpoint to insert a contact
    ├── fetchAllEntries.php    — GET endpoint to retrieve all contacts
    └── lookupEntry.php        — GET endpoint to search with statistics
```

---

## Database Schema

```sql
CREATE DATABASE IF NOT EXISTS ring_vault
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE ring_vault;

CREATE TABLE dial_entry (
    entry_id     INT AUTO_INCREMENT PRIMARY KEY,
    display_name VARCHAR(150) NOT NULL,
    dial_number  VARCHAR(50)  NOT NULL,
    entry_origin VARCHAR(50)  DEFAULT 'mobile',
    saved_at     DATETIME     DEFAULT CURRENT_TIMESTAMP
);
```

### Database Overview
<img width="1135" height="643" alt="image" src="https://github.com/user-attachments/assets/6bb4b198-d555-40cc-a0e6-b76091e1081e" />

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/storeEntry.php` | Insert one contact |
| GET | `/api/fetchAllEntries.php` | Retrieve all contacts |
| GET | `/api/lookupEntry.php?searchTerm=ali` | Search with stats |

### POST Body Example

```json
{
  "display_name": "Alice Martin",
  "dial_number": "+33612345678"
}
```

### Postman Test
<img width="718" height="860" alt="Postman test" src="https://github.com/user-attachments/assets/0e3c975d-ef8f-415e-ba6c-00caf804564d" />

### Search Response Example

```json
[
  {
    "display_name": "AliceMartin",
    "dial_number": "+33612345678",
    "occurrence": 3,
    "percentage": 75.0
  },
  {
    "display_name": "A.Martin",
    "dial_number": "+33612345678",
    "occurrence": 1,
    "percentage": 25.0
  }
]
```

---

## Key Concepts

| Concept | Where Used |
|---------|------------|
| `ContentResolver` | Reading contacts from the Android system |
| `READ_CONTACTS` permission | Requested at runtime via `ActivityResultLauncher` |
| `RecyclerView` + `Adapter` + `ViewHolder` | Displaying contact and search result lists |
| Retrofit2 | HTTP client for consuming the PHP REST API |
| Gson + `@SerializedName` | Automatic JSON ↔ Java object conversion |
| PDO prepared statements | Safe SQL queries preventing injection |
| `LIKE` pattern search | Partial matching on name and number |
| Statistical aggregation | `COUNT` + `GROUP BY` + percentage calculation |
| Singleton pattern | `VaultNetworkClient` reuses one Retrofit instance |
| Separation of concerns | config / model / service / api layers in PHP |

---

## Demo

https://github.com/user-attachments/assets/5d4f4983-9c8e-4a83-a5f0-436d7b8bf9eb

---

## How to Run

### Prerequisites
- Android Studio (Hedgehog or later)
- XAMPP with Apache + MySQL running
- PHP files placed in `htdocs/dialvault-api/`
- Android Emulator (API 24+)

### Setup
1. Import the SQL schema in phpMyAdmin
2. Place `dialvault-api/` folder in your `htdocs`
3. Open the Android project in Android Studio
4. Verify `VaultNetworkClient.java` uses `http://10.0.2.2/dialvault-api/api/`
5. Run the app on the emulator

---

## AndroidManifest.xml Requirements

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_CONTACTS" />

<application
    android:usesCleartextTraffic="true"
    ...>
```

---

## Dependencies

```kotlin
implementation("com.squareup.retrofit2:retrofit:2.11.0")
implementation("com.squareup.retrofit2:converter-gson:2.11.0")
implementation("androidx.recyclerview:recyclerview:1.3.2")
implementation("com.google.android.material:material:1.11.0")
```

---

## Author

**Laila Elamiri**  
GitHub: [@lailaelamiri](https://github.com/lailaelamiri/NumberBook)
