# Gym Backend - Yapılanlar Özeti (Things Done)

Bu doküman, Gym Backend projesinde geliştirilmiş olan özellikleri, sınıflar ve temel modüller bazında özetlemektedir.

## 1. Kullanıcı ve Yetkilendirme Yönetimi (User & Auth)
- **Oturum Yönetimi:** `Auth` sistemi üzerinden kullanıcı girişi (login) ve çıkışı (logout) yapılabilmekte, oturumlar güvenli HTTP-Only Cookie (`USER_SESSION`) üzerinden takip edilmektedir.
- **Kullanıcı Kaydı:** Sisteme `ADMIN` yetkisine sahip kullanıcılar tarafından yeni kullanıcılar (personel/admin) eklenebilmektedir. Şifreler özel güvenlik testlerinden geçirilmektedir (`@ValidPassword`).
- **Şifre Sıfırlama:** Kullanıcılar, kayıt aşamasında belirledikleri `backupSecret` (gizli kelime) ile unuttukları şifrelerini dışarıdan sıfırlayabilme özelliğine sahiptir.
- **Rol Bazlı Erişim (RBAC):** `ADMIN`, `CLERK` (Kasiyer/Sekreter) ve `REPAIRMAN` (Tamirci) rolleri ile endpoint düzeyinde güvenlik ve erişim sınırlamaları getirilmiştir.

## 2. Müşteri Yönetimi (Customer)
- **Müşteri Kaydı ve Düzenleme:** Sisteme müşteri eklenebilir, mevcut müşterilerin bilgileri (isim, soyisim, telefon) güncellenebilmektedir.
- **Telefon Doğrulaması:** Eklenen telefon numaralarının tam 11 haneli ve `0` ile başlayan bir yapıda olması düzenli ifadeler (Regex) ile zorunlu kılınmıştır.
- **Sağlık Raporu Süreçleri:** Müşterilerin spora uygunluğunu denetlemek için sağlık raporları sisteme PDF olarak yüklenebilmektedir.
- **Rapor İnceleme:** Yüklenen PDF raporlar indirilebilmekte, incelendikten sonra bir personel tarafından geçerlilik süresi (Bitiş Tarihi) girilerek onaylanıp doğrulanabilmektedir.

## 3. Abonelik ve Tarife Yönetimi (Subscription & Charge Profile)
- **Tarife / Profil Yönetimi (Charge Profile):** Yönetim tarafından sisteme standart üyelik fiyatları, tarifeler ve bu tarifelerin indirim / ücret çarpanları (Charge Rate) dinamik olarak CRUD operasyonları ile eklenebilmektedir.
- **Abonelik İlklendirme:** Yeni müşteriler için boş bir abonelik kaydı oluşturulabilmektedir.
- **Abonelik Satın Alımı:** Müşteriler, sistemdeki tarifeler üzerinden gün sayısı, aylık periyot sayısı gibi parametrelerle yeni abonelik satın alabilmektedir. 
- **Zaman Sınırlı Abonelikler:** İstendiğinde sadece belirli saatler (örn: 09:00 - 15:00 arası) geçerli olacak zaman sınırlı abonelikler oluşturulabilmektedir.
- **Abonelik İptali ve Geçmiş:** Aktif abonelikler iptal edilebilmekte ve kullanıcının geçmiş tüm satın alım kayıtları detaylı olarak listelenebilmektedir.

## 4. Makine Yönetimi (Machine)
- **Makine Envanteri:** Spor salonundaki aletlerin/makinelerin sisteme kaydı yapılabilmektedir. Ekleme sırasında makineye ait bir görsel (resim) de yüklenebilmektedir.
- **Bakım Periyotları:** Eklenen makineler için son bakım tarihi ve kaç ayda bir bakıma girmesi gerektiği (`maintenanceMonthlyPeriod`) bilgileri tutulmaktadır.
- **Görsel Yönetimi:** Sisteme yüklenen makine resimleri byte array üzerinden dışarı servis edilebilmektedir.

## 5. Bakım ve Onarım Yönetimi (Maintenance & Repair)
- **Bakım Kayıtları:** Tamirciler veya adminler, makinelere yapılan rutin bakımları sisteme maliyet (cost) ve detaylı bilgi (info) ile birlikte kaydedebilmektedir. 
- **Onarım Talepleri:** Arızalanan makineler için tahmini dönüş süresini ve onarım maliyetini içeren "Onarım Talepleri" (Repair) oluşturulabilmektedir.
- **Durum Takibi:** Onarımda olan makinelerin onarımı bittiğinde durumları güncellenebilmekte (`completeRepair`) ve geçmiş tüm onarım/bakım kayıtları geriye dönük incelenebilmektedir.

## 6. İstatistikler (Statistics)
- **Finansal İstatistikler:** Belirli bir zaman aralığı (`startDate` ve `endDate`) verilerek sistemdeki toplam gelir (satın alınan aboneliklerden), bakım masrafı ve onarım masrafı istatistikleri hesaplanıp tek bir servisle (`StatisticsController`) geri döndürülebilmektedir.
- **Tarih Kontrolleri:** İstatistik aramalarında ve diğer tüm tarih verilerinde özel bir `@ValidDate` validasyonu çalışmakta, artık yıllar dahil tarihlerin `dd/MM/yyyy` formatında doğru girilmesi sağlanmaktadır.

## 7. Otomatik Arka Plan İşlemleri (Scheduled Tasks)
- **Abonelik Süresi Kontrolü:** Her gün gece yarısı (00:00) çalışan arka plan servisi ile süresi dolan aboneliklerin durumu otomatik olarak güncellenmektedir.
- **Sağlık Raporu Süresi Kontrolü:** Her gün saat 01:00'da çalışan arka plan servisi ile bitiş tarihi (endDate) geçmiş olan sağlık raporlarının durumu `EXPIRED` (Süresi Dolmuş) olarak işaretlenmekte ve ilgili müşterinin durumu `PENDING` (Beklemede) statüsüne çekilmektedir.
- **Süresi Dolan Raporların Listelenmesi:** Süresi dolmuş olan tüm sağlık raporları ayrı bir API uç noktası üzerinden sorgulanabilmektedir.

## 8. Frontend Entegrasyonu için Eklenenler (2026-05-10)

`gym-frontend` operatör paneli ile uyumlu çalışmak için aşağıdaki iyileştirmeler yapılmıştır:

### 8.1. Yeni Endpoint
- **`GET /api/auth/me`** — Aktif `USER_SESSION` cookie'sini çözüp giriş yapmış kullanıcının `UserResponse`'unu döner. Frontend, login sonrası kullanıcının kim olduğunu öğrenmek için bu endpoint'i kullanır. Yetkisiz erişimde `401 unauthenticated` döner.

### 8.2. Düzeltilen Hatalar (Bug Fixes)
- **`ValidDateConstraint` regex bozukluğu:** Java string literal içinde dört kat kaçışlı backslash (`\\\\/`) ve sonda fazladan tırnak işareti vardı; geçerli `dd/MM/yyyy` tarihleri bile reddediliyordu. Regex temiz bir şekilde yeniden yazıldı, artık `01/04/2026` gibi geçerli tarihler doğru kabul ediliyor (`@ValidDate` artık beklendiği gibi çalışıyor).
- **`RepairServiceImpl` self-assignment:** `repairResponse.setEstimatedReturnDays(repairResponse.getEstimatedReturnDays())` üç farklı metotta `repairResponse` üstünden okuyup yazıyordu — sonuç hep `null` oluyordu. Düzeltme: `repair.getEstimatedReturnDays()` (entity'den okuma).
- **`Maintenance.maintainer` ve `Repair.maintainer` `@OneToOne` → `@ManyToOne`:** Bir teknisyen birden fazla bakım/onarım yapabileceği için `@OneToOne` yanlış unique constraint üretiyordu (ikinci bakım kaydı denendiğinde `unique index violation` veriliyordu). İlişki `@ManyToOne` olarak düzeltildi.

### 8.3. Demo Veri Seeder'ı (`DataStarter` Genişletildi)
- `application.properties`'e `gym.seed.demo=true` propertysi eklendi. Bu açık olduğunda ve veritabanı boş olduğunda `DataStarter` aşağıdaki demo veriyi otomatik yükler:
  - **6 personel** — 1 ana yönetici (`Ronnie Coleman`, ID 1) + 1 ek admin + 2 resepsiyon + 2 tekniker (hepsinin demo şifresi `Demo123!`, gizli kelimesi `demo`)
  - **6 tarife** — Aylık Standart, Aylık Öğrenci, Yıllık Premium, Sabah Kuşu, Gece Vardiyası, VIP Sınırsız
  - **22 üye** — Karışık `VERIFIED` / `PENDING` / `BLACKLIST` durumlarında, gerçekçi telefonlar
  - **Sağlık raporları** — VERIFIED ve PENDING üyeler için PDF placeholder + revisionDate/endDate
  - **Abonelikler ve satın alım kayıtları** — Aktif üyeler için tarife karışımı (saat sınırlı dahil)
  - **12 makine** — 10 `AVAILABLE` + 2 `ON_REPAIR_SERVICE`
  - **Bakım kayıtları** — Her makine için 1–3 rastgele bakım (gerçek personel referansı ile)
  - **Onarımlar** — 2 tamamlanmış + 2 açık onarım kaydı
- Üretim ortamına geçerken sadece `gym.seed.demo=false` yaparak demo veri yüklemesi devre dışı bırakılır; yine sadece tek bir admin (`Ronnie Coleman`) seed edilir.
