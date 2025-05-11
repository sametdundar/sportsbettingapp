# SportsBettingApp

Bu proje, Clean Architecture prensipleriyle geliştirilmiş modern bir spor bahis uygulamasıdır. Uygulama, Jetpack Compose ile yazılmıştır ve Hilt, Room, Retrofit gibi modern Android teknolojilerini kullanır.

## Özellikler
- 3 tab'lı modern bottom navigation bar
- Bülten ekranında sporlar, gruplar, arama ve oranlar
- Oran kutuları tıklanabilir ve seçili olduğunda sarı renkte
- Seçili oranlar FAB üzerinde maç sayısı olarak gösterilir
- Oran seçimi ve kupon işlemleri global state ile yönetilir
- Kupon oluşturma, silme ve Room veritabanına kaydetme
- Maç detay ekranı ve oran seçimi
- Hata yönetimi ve kullanıcı dostu arayüzler
- **Merkezi Analytics event yönetimi** (örnekler ve mimari aşağıda)

---

## Proje Mimarisi ve Klasör Yapısı

### Clean Architecture Katmanları
- **data/**: API, Room ve veri kaynakları, modellerin remote/local versiyonları, mapper'lar.
- **domain/**: Temel iş mantığı, entity ve usecase'ler.
- **presentation/**: ViewModel'ler, Compose UI ekranları, state/event yönetimi.
- **di/**: Hilt ile dependency injection modülleri, merkezi servisler.
- **MainViewModel.kt**: Uygulama genelinde seçili oranların ve kuponların yönetimi.
- **SportsBettingApp.kt**: Hilt Application sınıfı.

### Önemli Dosya ve Dizinler
- **data/remote/**: Retrofit API servisleri ve response modelleri
- **data/local/**: Room veritabanı, DAO ve typeconverter'lar
- **domain/model/**: Temel veri modelleri (Odds, SelectedBet, Coupon, vb.)
- **presentation/bulten/**: Bülten ekranı, ViewModel ve state yönetimi
- **presentation/eventdetail/**: Maç detay ekranı ve oran seçimi
- **presentation/mac/**: Kupon (sepet) ekranı ve işlemleri
- **presentation/kupon/**: Kaydedilmiş kuponlar ekranı
- **MainActivity.kt**: Navigation ve ana uygulama akışı
- **di/AnalyticsService.kt**: Analytics işlemleri için interface
- **di/FirebaseAnalyticsService.kt**: Firebase Analytics implementasyonu
- **di/BasketManager.kt**: Sepet yönetimi ve merkezi analytics loglama

### Kullanılan Temel Teknolojiler
- **Jetpack Compose**: Modern, deklaratif UI
- **Hilt**: Dependency Injection
- **Room**: Lokal veritabanı
- **Retrofit**: REST API entegrasyonu
- **Kotlin Coroutines & Flow**: Asenkron veri akışı
- **Firebase Analytics & Auth**: Kullanıcı ve event takibi

---

## Temel Akışlar
- **Bülten Ekranı**: Sporlar, gruplar ve maçlar listelenir. Oran kutularına tıklanarak seçim yapılır.
- **Oran Seçimi**: Seçili oranlar global olarak BasketManager ve MainViewModel ile yönetilir. FAB üzerinde maç sayısı gösterilir.
- **Maç Detay Ekranı**: Maçın detayları ve tüm marketler/oranlar gösterilir. Oran seçimi yapılabilir. Ekrana girişte analytics event loglanır.
- **Kupon (Sepet) Ekranı**: Seçili oranlar, kupon bedeli, toplam oran ve maksimum kazanç gösterilir. "Hemen Oyna" ile kupon kaydedilir.
- **Kuponlar Ekranı**: Room veritabanına kaydedilmiş kuponlar listelenir.
- **Analytics**: Maç detayına girildiğinde, sepete ekleme/çıkarma yapıldığında event gönderilir. Tüm analytics işlemleri merkezi bir servis üzerinden yönetilir.

---

## Merkezi Analytics Yönetimi

### 1. AnalyticsService (interface)
Tüm analytics işlemleri bu interface üzerinden yapılır. Örnek:
```kotlin
interface AnalyticsService {
    fun logEvent(eventName: String, params: Map<String, Any?> = emptyMap())
}
```

### 2. FirebaseAnalyticsService (implementasyon)
Firebase Analytics ile event loglamak için kullanılır. Hilt ile singleton olarak sağlanır.

### 3. BasketManager ve Diğer Kullanımlar
BasketManager, AnalyticsService'i constructor ile alır ve sepete ekleme/çıkarma işlemlerinde event loglar. Başka bir analytics servisi eklemek için sadece yeni bir implementasyon yazmak yeterlidir.

#### Genişletilebilirlik
- Başka bir analytics servisi (ör. Crashlytics, Amplitude, Mixpanel) eklemek için yeni bir implementasyon yazıp Hilt modülüne eklemeniz yeterli.
- Tüm eventler merkezi olarak yönetildiği için kod tekrarı ve karmaşıklık azalır.

---

## Kurulum

1. **Projeyi klonlayın:**
   ```bash
   git clone https://github.com/kullaniciadi/SportsBettingApp.git
   cd SportsBettingApp
   ```

2. **Gerekli araçlar:**
   - Android Studio (Giraffe veya üstü önerilir)
   - JDK 11+
   - Minimum SDK: 24

3. **Firebase Kurulumu:**
   - [Firebase Console](https://console.firebase.google.com/) üzerinden yeni bir proje oluşturun.
   - Android uygulamanızı ekleyin (package name: `com.sametdundar.sportsbettingapp`).
   - `google-services.json` dosyasını indirin ve `app/` klasörüne ekleyin.

4. **Projeyi derleyin ve çalıştırın.**

## Temel Kullanım
- Uygulama açıldığında bülten ekranı gelir.
- Oran kutularına tıklayarak seçim yapabilirsiniz.
- Seçili oranlar FAB üzerinde maç sayısı olarak görünür.
- Maç detayına tıklayarak detay ekranına geçebilirsiniz.
- "Hemen Oyna" ile kupon oluşturabilir, Room veritabanına kaydedebilirsiniz.

## Katkı ve Lisans
Katkıda bulunmak için pull request gönderebilirsiniz. Lisans bilgisi için lütfen proje sahibine danışın. 