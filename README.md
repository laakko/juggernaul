Task list
======

## Misc
- [x] **Kehitysympäristöt pystyyn ja projektin stubi GitLabiin. Projektin nimi on Juggernaul Ⓡ .**
- [ ] Ryhmän jakaminen uniikilla QR-koodilla
    - [ ] QR-koodin jakaminen esim. sähköpostilla, whatsappilla, telegramilla...

## Front-end
- [ ] Rakenna luokka `Task` yhdelle todo itemille, joka sisältää kaikki tarvittavat kentät: priority, DL, status, title, owner, ja vaihtoehtoiset kentät group, description
- [ ] `Task`illä pitää olla seuraavat statukset: todo, in progress, done, deleted. Yksilötilassa in progress ei ole tarpeen
- [ ] Tee runko UI:lle. @laakkoj2 tietää miten XML-hommat hoituu
    - [ ] Päänäkymä taulukko, jossa taskien sorttaus
    - [ ] Yksittäisen taskin avaaminen näyttää relevantit tiedot, jotka `Task`illä on
    - [ ] Aikajananäkymä, johon tulee pallurat/palkit oikeille päivämäärille. Näytetäänkö `Task`lle varattu "In progress" -aika, eli esim. 2vk aikajanalla jotenkin?

## Back-end
- [ ] SQLite kanta, joka hostaa ryhmät ja niiden datan. @jahnukj2 osaa
    - [ ] Kantarakenne ehkä seuraavanlainen:

    ```
    from GROUPS select JOINERID, USERID, TASKID
    from USERS select NAME, EMAIL
    from TASKS select PRIORITY, DEADLINE, USERID (vapaateksti tai sitten oikea USERID), DESCRIPTION, STATUS
    ```
