Task list
======

## Misc
- [ ] **Kehitysympäristöt pystyyn ja projektin stubi GitLabiin**
- [ ] Ryhmän jakaminen uniikilla QR-koodilla
    - [ ] QR-koodin jakaminen esim. sähköpostilla, whatsappilla, telegramilla...

## Front-end
- [ ] Rakenna luokka `TodoItem` yhdelle todo itemille, joka sisältää kaikki tarvittavat kentät: priority, DL, status, title, owner, ja vaihtoehtoiset kentät group, description
- [ ] `TodoItem`illä pitää olla seuraavat statukset: todo, in progress, done, deleted. Yksilötilassa in progress ei ole tarpeen
- [ ] Tee runko UI:lle. @laakkoj2 tietää miten XML-hommat hoituu
    - [ ] Päänäkymä taulukko, jossa taskien sorttaus
    - [ ] Yksittäisen taskin avaaminen näyttää relevantit tiedot, jotka `TodoItem`illä on
    - [ ] Aikajananäkymä, johon tulee pallurat/palkit oikeille päivämäärille. Näytetäänkö `TodoItem`lle varattu "In progress" -aika, eli esim. 2vk aikajanalla jotenkin?

## Back-end
- [ ] SQLite kanta, joka hostaa ryhmät ja niiden datan. @jahnukj2 osaa
