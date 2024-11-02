# sPvP Eklentisi

![Java](https://img.shields.io/badge/Java-8+-brightgreen)
![License](https://img.shields.io/badge/License-MIT-blue)

**sPvP**, Minecraft PvP deneyimini geliştirmek için özelleştirilebilir bir PvP eklentisidir. Çoklu dil desteği, kişisel öldürme mesajları ve dinamik oyun içi özelliklerle PvP deneyimini geliştirir!

## Özellikler

- 🌐 **Çoklu Dil Desteği** (`/lang/tr.yml`, `en.yml`, ve daha fazlası)
- 🔥 **Öldürme Serisi Mesajları**: Her öldürme serisi dönüm noktasında benzersiz mesajlar
- 🛠 **İzin Tabanlı Özel Mesajlar**: Oyuncu izinlerine göre öldürme mesajlarını özelleştirin (`perm:mesaj` formatı)
- 🎵 **Özelleştirilebilir ve Seçilebilir Kombo Sesleri**: Her kritik vuruş kombosu için ses ve pitch seçme, ses kategorileri ile genişletilmiş özelleştirme
- 🩸 **Can Göstergesi**: PvP sırasında rakibin canını ekranda, seçilen renkle gösterir
- ☠️ **Profil Menüsü**: Gelişmiş oyuncu profil menüsü
- ⚙️ **Özelleştirilebilir Ayarlar Menüsü**:
  - Profil
  - Kombo seslerini açıp kapatma
  - Kombo sesi seçme
  - Action bar kombo mesajlarını açıp kapatma
  - Rakibin canını gösterme açıp kapatma
  - Can göstergesi yazı rengini seçme
  - Özel öldürme mesajı seçme
- 🥶 **Oyuncu Dondurma**: Ceza komutu ayarlanabilir /freeze ve /unfreeze
- ⚔️ **Ölme Öldürme Log'u**: Gelişmiş ölme ve öldürme kayıtları
- 📊 **PlaceholderAPI Desteği**: Ölüm, öldürme, mevcut kombo sayısı, kombo rekoru, mevcut öldürme serisi ve öldürme serisi rekoru gibi istatistikleri PlaceholderAPI ile takip edin.

## Komutlar

| Komut                | Açıklama                                   | İzin                     | Varsayılan |
|----------------------|--------------------------------------------|--------------------------|------------|
| `/spvp`              | sPvP menüsünü açar                         | `spvp.commands.spvp`     | `true`     |
| `/profile <oyuncu>`  | Belirtilen oyuncunun profilini açar        | `spvp.commands.profile`  | `true`     |
| `/freeze <oyuncu>`   | Belirtilen oyuncuyu dondurur               | `spvp.commands.freeze`   | `op`        |
| `/unfreeze <oyuncu>` | Belirtilen oyuncunun dondurulmasını kaldır | `spvp.commands.unfreese` | `op`       |
| `/spvp reload`       | Eklentiyi yeniden yükler                   | `spvp.commands.reload`   | `op`       |

## İzinler

| İzin                     | Açıklama                              | Varsayılan |
|--------------------------|---------------------------------------| ---------- |
| `spvp.*`                 | Tüm izinleri verir                    | `op`       |
| `spvp.commands.*`        | Tüm komutlar için izin                | `op`       |
| `spvp.commands.reload`   | /spvp reload komutu izni              | `op`       |
| `spvp.commands.spvp`     | /spvp komutu izni                     | `true`     |
| `spvp.commands.profile`  | /profile komutu izni                  | `true`     |
| `spvp.commands.freeze`   | /freeze komutu izni                   | `true`     |
| `spvp.commands.unfreeze` | /unfreeze komutu izni                 | `true`     |
| `spvp.messages.*`        | Tüm özel öldürme mesajları için izin  | `op`     |
| `spvp.messages.<perm>`   | Belirli özel öldürme mesajı için izin | `op`     |

## PlaceholderAPI Desteği

| Placeholder               | Açıklama                                |
| ------------------------- | --------------------------------------- |
| `%spvp_kills%`            | Oyuncunun toplam öldürme sayısını gösterir |
| `%spvp_deaths%`           | Oyuncunun toplam ölüm sayısını gösterir   |
| `%spvp_current_combo%`    | Oyuncunun mevcut kombo sayısını gösterir  |
| `%spvp_combo_record%`     | Oyuncunun en yüksek kombo rekorunu gösterir |
| `%spvp_current_streak%`   | Oyuncunun mevcut öldürme serisini gösterir |
| `%spvp_streak_record%`    | Oyuncunun en yüksek öldürme serisi rekorunu gösterir |

## Kurulum

1. Son sürümü [Releases](https://github.com/username/sPvP/releases) sayfasından indirin.
2. `.jar` dosyasını sunucunuzun `plugins` klasörüne yerleştirin.
3. Sunucuyu yeniden başlatın veya `/reload` komutunu kullanarak eklentiyi etkinleştirin.
4. Dil dosyalarını ve ayarları `/lang` klasöründen ve ana yapılandırma dosyasından gerektiği gibi yapılandırın.

## Katkı

Katkılarınızı bekliyoruz! Hatalar veya özellik istekleri için bir issue açabilir ya da pull request gönderebilirsiniz.

## Lisans

Bu proje MIT Lisansı altında lisanslanmıştır - daha fazla bilgi için [LICENSE](LICENSE) dosyasına bakın.
