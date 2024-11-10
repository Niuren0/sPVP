# sPvP Eklentisi

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
- 🌍 **Dünya ve Bölge Engelleme**: Dünya ve bölgelerde engellleme (Bölge için WorldGuard gerekli).
- 🏆 **Gelişmiş Rank Sistemi**: Öldürdükçe puan kazanmalı ve aşamalı gelişmiş rank sistemi (LuckPerms gerekli).

## Komutlar

| Komut                            | Açıklama                                                | İzin                        | Varsayılan |
|----------------------------------|---------------------------------------------------------|-----------------------------|------------|
| `/spvp`                          | sPvP menüsünü açar                                      | `spvp.commands.spvp`        | `true`     |
| `/profile [oyuncu]`              | Belirtilen oyuncunun profilini açar                     | `spvp.commands.profile`     | `true`     |
| `/freeze <oyuncu>`               | Belirtilen oyuncuyu dondurur                            | `spvp.commands.freeze`      | `op`       |
| `/unfreeze <oyuncu>`             | Belirtilen oyuncunun dondurulmasını kaldır              | `spvp.commands.unfreese`    | `op`       |
| `/rank [oyuncu]`                 | Belirtilen oyuncunun rank bilgilerini gösterir          | `spvp.commands.rank`        | `true`     |
| `/rank set <oyuncu> <miktar>`    | Belirtilen oyuncunun puanın belirtilen miktara ayarlar  | `spvp.commands.rank.set`    | `op`        |
| `/rank add <oyuncu> <miktar>`    | Belirtilen oyuncunun puanın belirtilen miktara arttırır | `spvp.commands.rank.add`    | `op`        |
| `/rank remove <oyuncu> <miktar>` | Belirtilen oyuncunun puanın belirtilen miktara azaltır  | `spvp.commands.rank.remove` | `op`        |
| `/spvp reload`                   | Eklentiyi yeniden yükler                                | `spvp.commands.reload`      | `op`       |

## İzinler

| İzin                        | Açıklama                                    | Varsayılan |
|-----------------------------|---------------------------------------------|------------|
| `spvp.*`                    | Tüm izinleri verir                          | `op`       |
| `spvp.commands.*`           | Tüm komutlar için izin                      | `op`       |
| `spvp.commands.reload`      | /spvp reload komutu izni                    | `op`       |
| `spvp.commands.spvp`        | /spvp komutu izni                           | `true`     |
| `spvp.commands.profile`     | /profile komutu izni                        | `true`     |
| `spvp.commands.freeze`      | /freeze komutu izni                         | `true`     |
| `spvp.commands.unfreeze`    | /unfreeze komutu izni                       | `true`     |
| `spvp.commands.rank.*`      | Tüm rank komutları                          | `op`        |
| `spvp.commands.rank`        | Oyuncunun kendi rank bilgisi için izin      | `true`     |
| `spvp.commands.rank.others` | Başka oyuncuların rank bilgisine bakma izni | `true`     |
| `spvp.commands.rank.set`    | Rank puanı ayarlama izni                    | `op`     |
| `spvp.commands.rank.add`    | Rank puani arttırma izni                    | `op`     |
| `spvp.commands.rank.remove` | Rank puanı azaltma izni                     | `op`     |
| `spvp.messages.*`           | Tüm özel öldürme mesajları için izin        | `op`       |
| `spvp.messages.<perm>`      | Belirli özel öldürme mesajı için izin       | `op`       |

## PlaceholderAPI Desteği

| Placeholder                        | Açıklama                                                                          |
|------------------------------------|-----------------------------------------------------------------------------------|
| `%spvp_kills%`                     | Oyuncunun toplam öldürme sayısını gösterir                                        |
| `%spvp_deaths%`                    | Oyuncunun toplam ölüm sayısını gösterir                                           |
| `%spvp_kd%`                        | Oyuncunun kd değerini gösterir                                                    |
| `%spvp_current_combo%`             | Oyuncunun mevcut kombo sayısını gösterir                                          |
| `%spvp_combo_record%`              | Oyuncunun en yüksek kombo rekorunu gösterir                                       |
| `%spvp_current_streak%`            | Oyuncunun mevcut öldürme serisini gösterir                                        |
| `%spvp_streak_record%`             | Oyuncunun en yüksek öldürme serisi rekorunu gösterir                              |
| `%spvp_streak_record%`             | Oyuncunun en yüksek öldürme serisi rekorunu gösterir                              |
| `%spvp_rank_ranking%`              | Oyuncunun sunucudaki rank sıralamasını gösterir                                   |
| `%spvp_rank_top_<ranking>.name%`   | Sunucuda belirtilen sıradaki oyuncunun ismi (Ör.: %spvp_rank_top_1_name%)         |
| `%spvp_rank_top_<ranking>.points%` | Sunucuda belirtilen sıradaki oyuncunun rank puani (Ör.: %spvp_rank_top_1_points%) |

## Kurulum

1. Son sürümü [Releases](https://github.com/username/sPvP/releases) sayfasından indirin.
2. `.jar` dosyasını sunucunuzun `plugins` klasörüne yerleştirin.
3. Sunucuyu yeniden başlatın veya `/reload` komutunu kullanarak eklentiyi etkinleştirin.
4. Dil dosyalarını ve ayarları `/lang` klasöründen ve ana yapılandırma dosyasından gerektiği gibi yapılandırın.

## Katkı

Katkılarınızı bekliyoruz! Hatalar veya özellik istekleri için bir issue açabilir ya da pull request gönderebilirsiniz.

## Lisans

Bu proje MIT Lisansı altında lisanslanmıştır - daha fazla bilgi için [LICENSE](LICENSE) dosyasına bakın.
