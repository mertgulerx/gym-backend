# Frontend Endpoint Guide

Bu döküman, Gym Backend projesinin frontend entegrasyonu için gerekli tüm API uç noktalarını (endpoints), giriş-çıkış verilerini, yetkilendirme kurallarını ve hata yönetimini detaylı bir şekilde açıklamaktadır.

## 1. Global Hata Yönetimi (Exception Handling)

Backend, hatalı bir istek yapıldığında veya bir kural ihlal edildiğinde `RestException` aracılığıyla standart bir JSON formatında hata döner. Frontend ekibi API isteklerinde dönen hataları bu formata göre parse etmelidir.

**Hata Yanıt Formatı:**
```json
{
  "message": "Hatanın kısa özeti veya hata kodu (örn: wrong_date_format)",
  "details": {
    "field1": "Detaylı hata mesajı",
    "field2": "Detaylı hata mesajı"
  }
}
```
*Not: HTTP Status Code (Örn: 400 Bad Request, 401 Unauthorized, 404 Not Found) her zaman `RestException`'ın belirlediği koda uygun olarak döner.*

## 2. Özel Validasyon Kuralları (Validations)

Sistemde bazı alanlar için özel regex ve kurallar mevcuttur. Frontend tarafında form kontrolleri yapılırken bu kurallara birebir uyulması gerekmektedir.

### Tarih Formatı (`@ValidDate`)
- **Format:** `dd/MM/yyyy`
- **Uzunluk:** Tam olarak 10 karakter olmalıdır.
- **Kurallar:** 
  - Yıl bilgisi sadece `19xx` veya `20xx` olabilir.
  - Şubat ayı (Artık yıl dahil) gün kurallarına uymalıdır.
  - Örnek geçerli: `25/05/2023`, Örnek geçersiz: `25-05-2023` veya `25/05/23`

### Şifre Formatı (`@ValidPassword`)
- **Uzunluk:** Minimum 8, Maksimum 31 karakter.
- **Kurallar:**
  - En az 1 Büyük Harf (`A-Z`)
  - En az 1 Küçük Harf (`a-z`)
  - En az 1 Rakam (`0-9`)
  - En az 1 Özel Karakter (`!@#$%^&*()_+-=[]{};':"\\|,.<>/?`)
  - **Boşluk karakteri (Space) kesinlikle içeremez.**

### Enum Formatı (`@ValidEnum`)
- Gönderilen string değer, backend'deki Enum değerleriyle birebir aynı olmalıdır (Büyük harf duyarlıdır, örn: `ADMIN`, `CLERK`, `REPAIRMAN`).

---

## 3. Endpoints (API Uç Noktaları)

Tüm endpoint'ler `http://<domain>/api` temel dizini altındadır.

### 3.1. AuthController (`/api/auth`)

#### 3.1.1. Kullanıcı Girişi
- **Endpoint:** `POST /api/auth/login`
- **Rol:** Herkese Açık
- **Girdi (Body):** `LoginRequest`
  ```json
  {
    "id": 12345, // Min: 0, Max: 1000000
    "password": "Password123!" // Min 8, Max 100 Karakter (ValidPassword kurallarına uygun değil sadece uzunluk)
  }
  ```
- **Çıktı:** `ApiResponse` (Ayrıca `USER_SESSION` adında bir HTTP-Only Cookie döner)
  ```json
  {
    "success": true,
    "message": "login_success"
  }
  ```

#### 3.1.2. Çıkış Yapma
- **Endpoint:** `POST /api/auth/logout`
- **Rol:** Oturum açmış kullanıcılar
- **Çıktı:** `ApiResponse` (Cookie sıfırlanır)

---

### 3.2. UserController (`/api/user`)

#### 3.2.1. Personel / Kullanıcı Kaydı
- **Endpoint:** `POST /api/user/register`
- **Rol:** Sadece `ADMIN`
- **Girdi (Body):** `UserRegisterRequest`
  ```json
  {
    "userType": "ADMIN", // ValidEnum (UserRole)
    "password": "ValidPassword1!", // ValidPassword kurallarına uygun
    "name": "Mert",
    "surName": "Güler",
    "backupSecret": "gizli_kelime"
  }
  ```
- **Çıktı:** `UserRegisterResponse`
  ```json
  {
    "id": 123
  }
  ```

#### 3.2.2. Şifre Sıfırlama
- **Endpoint:** `POST /api/user/password-reset`
- **Rol:** Herkese Açık (Doğru ID ve Secret bilinmesi şartıyla)
- **Query Parametreleri:**
  - `id`: Kullanıcı ID
  - `backupSecret`: Kullanıcının güvenlik kelimesi
  - `newPassword`: Yeni şifre (`@ValidPassword` kurallarına uymalı)
- **Çıktı:** `ApiResponse` (success: true/false döner)

#### 3.2.3. Kullanıcı Bilgisi Getirme
- **Endpoint:** `GET /api/user/{id}`
- **Rol:** Sadece `ADMIN`
- **Çıktı:** `UserResponse`

#### 3.2.4. Tüm Kullanıcıları Getirme
- **Endpoint:** `GET /api/user`
- **Rol:** Sadece `ADMIN`
- **Çıktı:** `List<UserResponse>`

---

### 3.3. CustomerController (`/api/customer`)

#### 3.3.1. Müşteri Kaydı
- **Endpoint:** `POST /api/customer/register`
- **Rol:** `ADMIN`, `CLERK`
- **Girdi (Body):** `CustomerRegisterRequest`
  ```json
  {
    "name": "Ali",
    "surName": "Yılmaz",
    "phoneNumber": "05554443322" // Tam 11 karakter, ^[0][0-9]{10}$ regex'ine uygun (0 ile başlamalı)
  }
  ```
- **Çıktı:** `CustomerRegisterResponse`

#### 3.3.2. Müşteri Güncelleme
- **Endpoint:** `PUT /api/customer/{id}`
- **Rol:** `ADMIN`, `CLERK`
- **Girdi (Body):** `CustomerRegisterRequest` (Kayıt ile aynı veriler)
- **Çıktı:** `CustomerResponse`

#### 3.3.3. Müşterileri Listeleme ve Detay
- **Tekil Getirme:** `GET /api/customer/{id}` (Çıktı: `CustomerResponse`)
- **Tümünü Getirme:** `GET /api/customer` (Çıktı: `List<CustomerResponse>`)
- **Rol:** `ADMIN`, `CLERK`

#### 3.3.4. Sağlık Raporu Yükleme
- **Endpoint:** `POST /api/customer/{id}/health_report`
- **Rol:** `ADMIN`, `CLERK`
- **Girdi (Form-Data):** `file` (Mutlaka PDF olmalıdır `application/pdf`)
- **Çıktı:** `ApiResponse`

#### 3.3.5. Sağlık Raporu İndirme
- **Endpoint:** `GET /api/customer/{id}/health_report/document`
- **Rol:** `ADMIN`, `CLERK`
- **Çıktı:** PDF dosyası (`ByteArrayResource`)

#### 3.3.6. Sağlık Raporu Bilgisi
- **Endpoint:** `GET /api/customer/{id}/health_report`
- **Rol:** `ADMIN`, `CLERK`
- **Çıktı:** `CustomerHealthReportResponse`

#### 3.3.7. Sağlık Raporu Doğrulama
- **Endpoint:** `PUT /api/customer/{id}/health_report/verify`
- **Rol:** `ADMIN`, `CLERK`
- **Query Parametresi:** `revisionDate` (`dd/MM/yyyy` formatında `@ValidDate`)
- **Çıktı:** `ApiResponse`

---

### 3.4. MachineController (`/api/machine`)

#### 3.4.1. Makine Ekleme
- **Endpoint:** `POST /api/machine/create`
- **Rol:** `ADMIN`
- **Girdi (Multipart/Form-Data):** 
  - `request`: JSON objesi (MachineCreateRequest)
    ```json
    {
      "name": "Koşu Bandı", // Max 200 karakter
      "lastMaintenanceDate": "25/05/2023", // @ValidDate
      "maintenanceMonthlyPeriod": 6 // Min 1, Max 128
    }
    ```
  - `file`: MultipartFile resim dosyası
- **Çıktı:** `MachineResponse`

#### 3.4.2. Makine Resmi İndirme
- **Endpoint:** `GET /api/machine/{id}/image`
- **Rol:** `ADMIN`, `REPAIRMAN`
- **Çıktı:** JPEG resim dosyası (`ByteArrayResource`)

#### 3.4.3. Makineleri Listeleme
- **Tekil:** `GET /api/machine/{id}`
- **Tümü:** `GET /api/machine`
- **Rol:** `ADMIN`, `REPAIRMAN`
- **Çıktı:** `MachineResponse` / `List<MachineResponse>`

---

### 3.5. MaintenanceController (`/api/machine/{machineId}/maintenance`)

#### 3.5.1. Bakım Ekleme
- **Endpoint:** `POST /api/machine/{machineId}/maintenance`
- **Rol:** `ADMIN`, `REPAIRMAN`
- **Girdi (Body):** `MaintenanceCreateRequest`
  ```json
  {
    "cost": 1500.50, // Min 0
    "info": "Yağlama ve kayış değişimi"
  }
  ```
- **Çıktı:** `MaintenanceResponse`

#### 3.5.2. Bakım Geçmişi Görme
- **Son Bakım:** `GET /api/machine/{machineId}/maintenance/last`
- **Tüm Bakımlar:** `GET /api/machine/{machineId}/maintenance/all`
- **Rol:** `ADMIN`, `REPAIRMAN`
- **Çıktı:** `MaintenanceResponse` / `List<MaintenanceResponse>`

---

### 3.6. RepairController (`/api/machine/{machineId}/repair`)

#### 3.6.1. Onarım Talebi Ekleme
- **Endpoint:** `POST /api/machine/{machineId}/repair`
- **Rol:** `ADMIN`, `REPAIRMAN`
- **Girdi (Body):** `RepairCreateRequest`
  ```json
  {
    "cost": 2500.00, // Min 0
    "info": "Motor arızası",
    "estimatedReturnDays": 5,
    "isCompleted": false
  }
  ```
- **Çıktı:** `RepairResponse`

#### 3.6.2. Onarımı Tamamlama
- **Endpoint:** `PUT /api/machine/{machineId}/repair/complete`
- **Rol:** `ADMIN`, `REPAIRMAN`
- **Çıktı:** `ApiResponse`

#### 3.6.3. Onarım Geçmişi
- **Son Onarım:** `GET /api/machine/{machineId}/repair/last`
- **Tüm Onarımlar:** `GET /api/machine/{machineId}/repair/all`
- **Rol:** `ADMIN`, `REPAIRMAN`
- **Çıktı:** `RepairResponse` / `List<RepairResponse>`

---

### 3.7. ChargeProfileController (`/api/charge-profile`)

#### 3.7.1. Tarife / Profil İşlemleri
- **Oluşturma:** `POST /api/charge-profile/create` (Rol: `ADMIN`)
- **Güncelleme:** `PUT /api/charge-profile/{id}` (Rol: `ADMIN`, `CLERK`)
- **Silme:** `DELETE /api/charge-profile/{id}` (Rol: `ADMIN`, `CLERK`)
- **Girdi (Body):** `ChargeProfileCreateRequest`
  ```json
  {
    "title": "Aylık Standart",
    "info": "Standart üyelik planı",
    "chargeRate": 1.0,
    "chargeCost": 1500.00
  }
  ```
- **Tekil Getirme:** `GET /api/charge-profile/{id}` (Rol: `ADMIN`, `CLERK`)
- **Tümünü Getirme:** `GET /api/charge-profile/charge-profile` (Rol: `ADMIN`, `CLERK`)

---

### 3.8. SubscriptionController (`/api/subscription`)

#### 3.8.1. Abonelik Başlatma/İlklendirme
- **Endpoint:** `POST /api/subscription/{customerId}/initialize`
- **Rol:** `ADMIN`, `CLERK`
- **Çıktı:** `ApiResponse`

#### 3.8.2. Abonelik Satın Alma
- **Endpoint:** `POST /api/subscription/{customerId}/purchase/create`
- **Rol:** `ADMIN`, `CLERK`
- **Girdi (Body):** `SubscriptionPurchaseRequest`
  ```json
  {
    "title": "Aylık Öğrenci",
    "subscriptionDays": 30, // Min 8, Max 30
    "subscriptionMonthPeriod": 1, // Min 1
    "chargeRate": 0.8,
    "chargeCost": 1200.00,
    "isTimeLimited": true,
    "startHour": 9, // isTimeLimited true ise girilebilir
    "endHour": 15
  }
  ```
- **Çıktı:** `SubscriptionPurchaseResponse`

#### 3.8.3. Abonelik İptali ve Bilgileri
- **İptal Etme:** `PUT /api/subscription/{customerId}`
- **Mevcut Abonelik Durumu:** `GET /api/subscription/{customerId}` (Çıktı: `SubscriptionResponse`)
- **Rol:** `ADMIN`, `CLERK`

#### 3.8.4. Satın Alım Geçmişi
- **Son Satın Alım:** `GET /api/subscription/{customerId}/last`
- **Tüm Satın Alımlar:** `GET /api/subscription/{customerId}/all`
- **Rol:** `ADMIN`, `CLERK`
- **Çıktı:** `SubscriptionPurchaseResponse` / `List<SubscriptionPurchaseResponse>`

---

### 3.9. StatisticsController (`/api/statistics`)

#### 3.9.1. İstatistikleri Getirme
- **Endpoint:** `GET /api/statistics`
- **Rol:** Sadece `ADMIN`
- **Query Parametreleri:**
  - `startDate`: `dd/MM/yyyy` formatında (`@ValidDate`)
  - `endDate`: `dd/MM/yyyy` formatında (`@ValidDate`)
- **Çıktı:** `StatisticsResponse` (Belirtilen zaman aralığındaki gelir, bakım masrafı ve onarım masraflarını döner)
