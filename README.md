Task list
======

## Misc
- [x] **Kehitysympäristöt pystyyn ja projektin stubi GitLabiin. Projektin nimi on Juggernaul Ⓡ .**
- [ ] Ryhmän jakaminen uniikilla QR-koodilla
    - [x] QR-koodin generointi
    - [ ] QR-koodissa backendin luoma tokeni
    - [ ] QR-koodin jakaminen esim. sähköpostilla, whatsappilla, telegramilla...
- [ ] Värimaailma, kuvakkeet

## Front-end
- [x] Rakenna luokka `Task` yhdelle todo itemille, joka sisältää kaikki tarvittavat kentät: priority, DL, status, title, owner, ja vaihtoehtoiset kentät group, description  - In progress (@vikstri1)
- [x] `Task`illä pitää olla seuraavat statukset: todo, in progress, done, deleted. Yksilötilassa in progress ei ole tarpeen
    - [ ] 'Task'in statuksen vaihtaminen swipellä listassa. Vasemmalle poistaa ja oikealle vaihtaa todo -> in progress -> done
- [x] Taskien (listan) tallentaminen lokaalisti
- [ ] Kirjautumis toiminnallisuus (Sposti, salasana)
- [ ] Taskien ehdottaminen HomeTabissa
- [ ] Erilliset listat eri kategorioille (Niinkuin siinä Ilmarin käyttämässä ToDo appissa)
- [x] Tee runko UI:lle. @laakkoj2 tietää miten XML-hommat hoituu
    - [x] Päänäkymä taulukko
    - [ ] Taskien sorttaus (aika, prioriteetti, status, aakkosjärjestys tms)
    - [ ] Asetukset - näkymä
    - [ ] Kirjautumis näkymä
    - [x] Yksittäisen taskin avaaminen näyttää relevantit tiedot, jotka `Task`illä on
    - [ ] Taskin värin vaihtuminen prioriteetin mukaan
    - [ ] Aikajananäkymä, johon tulee pallurat/palkit oikeille päivämäärille. Näytetäänkö `Task`lle varattu "In progress" -aika, eli esim. 2vk aikajanalla jotenkin?
    - [ ] Tehdyt taskit eroteltu tekemättömistä

## Back-end

- [ ] Aallon serveri pystyyn
- [ ] SQLite kanta, joka hostaa ryhmät ja niiden datan. @jahnukj2 osaa
