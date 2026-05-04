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
