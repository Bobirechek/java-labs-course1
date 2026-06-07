package managers;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.UIManager;

public class LocaleManager {

    public static final Locale RUSSIAN = new Locale("ru", "RU");
    public static final Locale GERMAN = new Locale("de", "DE");
    public static final Locale HUNGARIAN = new Locale("hu", "HU");
    public static final Locale ENGLISH_CA = new Locale("en", "CA");

    public static final List<Locale> SUPPORTED_LOCALES = Arrays.asList(
            RUSSIAN, GERMAN, HUNGARIAN, ENGLISH_CA
    );

    private static Locale currentLocale = RUSSIAN;
    private static final List<Runnable> listeners = new CopyOnWriteArrayList<>();

    // ─────────────────── строки приложения ───────────────────
    private static final Map<String, Map<String, String>> STRINGS = new HashMap<>();

    static {
        // --- РУССКИЙ ---
        Map<String, String> ru = new HashMap<>();
        ru.put("app.title",           "Коллекция HumanBeing");
        ru.put("auth.title",          "Авторизация");
        ru.put("auth.login",          "Логин");
        ru.put("auth.password",       "Пароль");
        ru.put("auth.btn.login",      "Войти");
        ru.put("auth.btn.register",   "Зарегистрироваться");
        ru.put("auth.error.empty",    "Логин и пароль не могут быть пустыми");
        ru.put("auth.error.conn",     "Нет связи с сервером");
        ru.put("main.tab.table",      "Таблица");
        ru.put("main.tab.visual",     "Визуализация");
        ru.put("main.btn.add",        "Добавить");
        ru.put("main.btn.edit",       "Редактировать");
        ru.put("main.btn.delete",     "Удалить");
        ru.put("main.btn.refresh",    "Обновить");
        ru.put("main.btn.clear",      "Очистить (свои)");
        ru.put("main.btn.sort",       "Сортировать");
        ru.put("main.btn.reorder",    "Реорганизовать");
        ru.put("main.btn.info",       "Информация");
        ru.put("main.btn.help",       "Помощь");
        ru.put("main.btn.logout",     "Выйти");
        ru.put("main.btn.exit",       "Закрыть");
        ru.put("main.filter.label",   "Фильтр:");
        ru.put("main.filter.col",     "по колонке:");
        ru.put("main.user.label",     "Пользователь:");
        ru.put("main.lang.label",     "Язык:");
        ru.put("main.status.ready",   "Готово");
        ru.put("main.status.loading", "Загрузка...");
        ru.put("col.id",              "ID");
        ru.put("col.name",            "Имя");
        ru.put("col.x",               "X");
        ru.put("col.y",               "Y");
        ru.put("col.created",         "Создано");
        ru.put("col.realHero",        "Реальный герой");
        ru.put("col.toothpick",       "Зубочистка");
        ru.put("col.speed",           "Скорость удара");
        ru.put("col.soundtrack",      "Саундтрек");
        ru.put("col.weapon",          "Оружие");
        ru.put("col.mood",            "Настроение");
        ru.put("col.car",             "Машина");
        ru.put("col.owner",           "Владелец");
        ru.put("dlg.add.title",       "Добавить HumanBeing");
        ru.put("dlg.edit.title",      "Редактировать HumanBeing");
        ru.put("dlg.btn.save",        "Сохранить");
        ru.put("dlg.btn.cancel",      "Отмена");
        ru.put("dlg.field.name",      "Имя *");
        ru.put("dlg.field.x",         "Координата X *");
        ru.put("dlg.field.y",         "Координата Y *");
        ru.put("dlg.field.realHero",  "Реальный герой");
        ru.put("dlg.field.toothpick", "Есть зубочистка");
        ru.put("dlg.field.speed",     "Скорость удара (≤597) *");
        ru.put("dlg.field.soundtrack","Саундтрек *");
        ru.put("dlg.field.weapon",    "Тип оружия");
        ru.put("dlg.field.mood",      "Настроение");
        ru.put("dlg.field.carName",   "Название машины");
        ru.put("dlg.field.carCool",   "Крутая машина");
        ru.put("msg.deleted",         "Объект удалён");
        ru.put("msg.noselect",        "Не выбран объект");
        ru.put("msg.notowner",        "Вы не владелец этого объекта");
        ru.put("msg.confirm.delete",  "Удалить выбранный объект?");
        ru.put("msg.confirm.title",   "Подтверждение");
        ru.put("msg.server.error",    "Ошибка сервера");
        ru.put("cmd.count_soundtrack","Фильтр по саундтреку:");
        ru.put("cmd.filter_name",     "Фильтр по имени (содержит):");
        ru.put("cmd.remove_greater",  "Удалить больше по скорости:");
        ru.put("cmd.dialog.title",    "Выполнить команду");
        ru.put("cmd.dialog.input",    "Введите аргумент:");
        ru.put("vis.click.info",      "Кликните на объект для информации");
        ru.put("main.sort.label",   "Сортировка:");
        ru.put("vis.control.hint",  "ПКМ+тащи=пан  Колесо=масштаб  ЛКМ=выбор");
        ru.put("help.text",
        "Доступные команды:\n" +
        "  help                              — вывести помощь\n" +
        "  info                              — информация о коллекции\n" +
        "  show                              — показать все объекты\n" +
        "  add                               — добавить новый объект\n" +
        "  update <id>                       — обновить объект по ID\n" +
        "  remove_by_id <id>                 — удалить объект по ID\n" +
        "  clear                             — очистить свои объекты\n" +
        "  sort                              — сортировать коллекцию\n" +
        "  reorder                           — перевернуть порядок\n" +
        "  remove_greater                    — удалить объекты больше заданного\n" +
        "  count_by_soundtrack_name <name>   — количество по саундтреку\n" +
        "  filter_contains_name <name>       — фильтр по имени\n" +
        "  print_field_descending_impact_speed — скорость по убыванию\n" +
        "  execute_script <file>             — выполнить скрипт"
    );
        // Ошибки валидации
        ru.put("err.empty",            "поле не может быть пустым");
        ru.put("err.long",             "введите целое число");
        ru.put("err.double",           "введите число (например: 10.5)");
        ru.put("err.speed_max",        "значение не может превышать 597");
        ru.put("err.speed_neg",        "значение не может быть отрицательным");
        ru.put("err.too_long",         "значение слишком длинное (макс. 200 символов)");
        ru.put("err.unexpected",       "Неожиданная ошибка");
        ru.put("err.validation_title", "Ошибка ввода");
        ru.put("err.server_null",      "Сервер не вернул ответ. Попробуйте снова.");
        STRINGS.put("ru", ru);

        // --- НЕМЕЦКИЙ ---
        Map<String, String> de = new HashMap<>(ru);
        de.put("app.title",           "HumanBeing-Sammlung");
        de.put("auth.title",          "Anmeldung");
        de.put("auth.login",          "Benutzername");
        de.put("auth.password",       "Passwort");
        de.put("auth.btn.login",      "Anmelden");
        de.put("auth.btn.register",   "Registrieren");
        de.put("auth.error.empty",    "Benutzername und Passwort dürfen nicht leer sein");
        de.put("auth.error.conn",     "Keine Verbindung zum Server");
        de.put("main.tab.table",      "Tabelle");
        de.put("main.tab.visual",     "Visualisierung");
        de.put("main.btn.add",        "Hinzufügen");
        de.put("main.btn.edit",       "Bearbeiten");
        de.put("main.btn.delete",     "Löschen");
        de.put("main.btn.refresh",    "Aktualisieren");
        de.put("main.btn.clear",      "Eigene löschen");
        de.put("main.btn.sort",       "Sortieren");
        de.put("main.btn.reorder",    "Neu ordnen");
        de.put("main.btn.info",       "Information");
        de.put("main.btn.help",       "Hilfe");
        de.put("main.btn.logout",     "Abmelden");
        de.put("main.btn.exit",       "Beenden");
        de.put("main.filter.label",   "Filter:");
        de.put("main.filter.col",     "nach Spalte:");
        de.put("main.user.label",     "Benutzer:");
        de.put("main.lang.label",     "Sprache:");
        de.put("main.status.ready",   "Bereit");
        de.put("main.status.loading", "Laden...");
        de.put("col.id",              "ID");
        de.put("col.name",            "Name");
        de.put("col.x",               "X");
        de.put("col.y",               "Y");
        de.put("col.created",         "Erstellt");
        de.put("col.realHero",        "Echter Held");
        de.put("col.toothpick",       "Zahnstocher");
        de.put("col.speed",           "Aufprallgeschwindigkeit");
        de.put("col.soundtrack",      "Soundtrack");
        de.put("col.weapon",          "Waffe");
        de.put("col.mood",            "Stimmung");
        de.put("col.car",             "Auto");
        de.put("col.owner",           "Eigentümer");
        de.put("dlg.add.title",       "HumanBeing hinzufügen");
        de.put("dlg.edit.title",      "HumanBeing bearbeiten");
        de.put("dlg.btn.save",        "Speichern");
        de.put("dlg.btn.cancel",      "Abbrechen");
        de.put("dlg.field.name",      "Name *");
        de.put("dlg.field.x",         "Koordinate X *");
        de.put("dlg.field.y",         "Koordinate Y *");
        de.put("dlg.field.realHero",  "Echter Held");
        de.put("dlg.field.toothpick", "Hat Zahnstocher");
        de.put("dlg.field.speed",     "Aufprallgeschwindigkeit (≤597) *");
        de.put("dlg.field.soundtrack","Soundtrack *");
        de.put("dlg.field.weapon",    "Waffentyp");
        de.put("dlg.field.mood",      "Stimmung");
        de.put("dlg.field.carName",   "Autoname");
        de.put("dlg.field.carCool",   "Cooles Auto");
        de.put("msg.deleted",         "Objekt gelöscht");
        de.put("msg.noselect",        "Kein Objekt ausgewählt");
        de.put("msg.notowner",        "Sie sind nicht der Eigentümer");
        de.put("msg.confirm.delete",  "Ausgewähltes Objekt löschen?");
        de.put("msg.confirm.title",   "Bestätigung");
        de.put("msg.server.error",    "Serverfehler");
        de.put("cmd.count_soundtrack","Nach Soundtrack filtern:");
        de.put("cmd.filter_name",     "Nach Name filtern (enthält):");
        de.put("cmd.remove_greater",  "Größere nach Geschwindigkeit entfernen:");
        de.put("cmd.dialog.title",    "Befehl ausführen");
        de.put("cmd.dialog.input",    "Argument eingeben:");
        de.put("vis.click.info",      "Klicken Sie auf ein Objekt für Informationen");
        de.put("main.sort.label",   "Sortierung:");
        de.put("vis.control.hint",  "RMT+ziehen=schwenken  Rad=Zoom  LMT=Auswahl");
        de.put("help.text",
        "Verfügbare Befehle:\n" +
        "  help                              — Hilfe anzeigen\n" +
        "  info                              — Informationen zur Sammlung\n" +
        "  show                              — alle Objekte anzeigen\n" +
        "  add                               — neues Objekt hinzufügen\n" +
        "  update <id>                       — Objekt nach ID aktualisieren\n" +
        "  remove_by_id <id>                 — Objekt nach ID löschen\n" +
        "  clear                             — eigene Objekte löschen\n" +
        "  sort                              — Sammlung sortieren\n" +
        "  reorder                           — Reihenfolge umkehren\n" +
        "  remove_greater                    — größere Objekte entfernen\n" +
        "  count_by_soundtrack_name <name>   — Anzahl nach Soundtrack\n" +
        "  filter_contains_name <name>       — nach Name filtern\n" +
        "  print_field_descending_impact_speed — Geschwindigkeit absteigend\n" +
        "  execute_script <file>             — Skript ausführen"
    );
        de.put("err.empty",            "Feld darf nicht leer sein");
        de.put("err.long",             "Bitte ganze Zahl eingeben");
        de.put("err.double",           "Bitte Zahl eingeben (z.B.: 10.5)");
        de.put("err.speed_max",        "Wert darf 597 nicht überschreiten");
        de.put("err.speed_neg",        "Wert darf nicht negativ sein");
        de.put("err.too_long",         "Wert zu lang (max. 200 Zeichen)");
        de.put("err.unexpected",       "Unerwarteter Fehler");
        de.put("err.validation_title", "Eingabefehler");
        de.put("err.server_null",      "Server hat nicht geantwortet. Bitte erneut versuchen.");
        STRINGS.put("de", de);

        // --- ВЕНГЕРСКИЙ ---
        Map<String, String> hu = new HashMap<>(ru);
        hu.put("app.title",           "HumanBeing gyűjtemény");
        hu.put("auth.title",          "Bejelentkezés");
        hu.put("auth.login",          "Felhasználónév");
        hu.put("auth.password",       "Jelszó");
        hu.put("auth.btn.login",      "Bejelentkezés");
        hu.put("auth.btn.register",   "Regisztráció");
        hu.put("auth.error.empty",    "A felhasználónév és jelszó nem lehet üres");
        hu.put("auth.error.conn",     "Nincs kapcsolat a szerverrel");
        hu.put("main.tab.table",      "Táblázat");
        hu.put("main.tab.visual",     "Vizualizáció");
        hu.put("main.btn.add",        "Hozzáadás");
        hu.put("main.btn.edit",       "Szerkesztés");
        hu.put("main.btn.delete",     "Törlés");
        hu.put("main.btn.refresh",    "Frissítés");
        hu.put("main.btn.clear",      "Saját törlése");
        hu.put("main.btn.sort",       "Rendezés");
        hu.put("main.btn.reorder",    "Újrarendezés");
        hu.put("main.btn.info",       "Információ");
        hu.put("main.btn.help",       "Súgó");
        hu.put("main.btn.logout",     "Kijelentkezés");
        hu.put("main.btn.exit",       "Kilépés");
        hu.put("main.filter.label",   "Szűrő:");
        hu.put("main.filter.col",     "oszlop szerint:");
        hu.put("main.user.label",     "Felhasználó:");
        hu.put("main.lang.label",     "Nyelv:");
        hu.put("main.status.ready",   "Kész");
        hu.put("main.status.loading", "Betöltés...");
        hu.put("col.id",              "ID");
        hu.put("col.name",            "Név");
        hu.put("col.x",               "X");
        hu.put("col.y",               "Y");
        hu.put("col.created",         "Létrehozva");
        hu.put("col.realHero",        "Igazi hős");
        hu.put("col.toothpick",       "Fogpiszkaló");
        hu.put("col.speed",           "Ütközési sebesség");
        hu.put("col.soundtrack",      "Zeneszám");
        hu.put("col.weapon",          "Fegyver");
        hu.put("col.mood",            "Hangulat");
        hu.put("col.car",             "Autó");
        hu.put("col.owner",           "Tulajdonos");
        hu.put("dlg.add.title",       "HumanBeing hozzáadása");
        hu.put("dlg.edit.title",      "HumanBeing szerkesztése");
        hu.put("dlg.btn.save",        "Mentés");
        hu.put("dlg.btn.cancel",      "Mégse");
        hu.put("dlg.field.name",      "Név *");
        hu.put("dlg.field.x",         "X koordináta *");
        hu.put("dlg.field.y",         "Y koordináta *");
        hu.put("dlg.field.realHero",  "Igazi hős");
        hu.put("dlg.field.toothpick", "Van fogpiszkalója");
        hu.put("dlg.field.speed",     "Ütközési sebesség (≤597) *");
        hu.put("dlg.field.soundtrack","Zeneszám *");
        hu.put("dlg.field.weapon",    "Fegyvertípus");
        hu.put("dlg.field.mood",      "Hangulat");
        hu.put("dlg.field.carName",   "Autó neve");
        hu.put("dlg.field.carCool",   "Menő autó");
        hu.put("msg.deleted",         "Objektum törölve");
        hu.put("msg.noselect",        "Nincs kiválasztott objektum");
        hu.put("msg.notowner",        "Ön nem a tulajdonos");
        hu.put("msg.confirm.delete",  "Törölje a kiválasztott objektumot?");
        hu.put("msg.confirm.title",   "Megerősítés");
        hu.put("msg.server.error",    "Szerverhiba");
        hu.put("cmd.count_soundtrack","Szűrés zeneszám szerint:");
        hu.put("cmd.filter_name",     "Szűrés név szerint (tartalmazza):");
        hu.put("cmd.remove_greater",  "Nagyobb eltávolítása sebesség szerint:");
        hu.put("cmd.dialog.title",    "Parancs végrehajtása");
        hu.put("cmd.dialog.input",    "Adja meg az argumentumot:");
        hu.put("vis.click.info",      "Kattintson egy objektumra az információért");
        hu.put("main.sort.label",   "Rendezés:");
        hu.put("vis.control.hint",  "JKL+húzás=mozgás  Görgetés=zoom  BKL=kiválaszt");
        hu.put("help.text",
        "Elérhető parancsok:\n" +
        "  help                              — súgó megjelenítése\n" +
        "  info                              — gyűjtemény információi\n" +
        "  show                              — összes objektum megjelenítése\n" +
        "  add                               — új objektum hozzáadása\n" +
        "  update <id>                       — objektum frissítése ID alapján\n" +
        "  remove_by_id <id>                 — objektum törlése ID alapján\n" +
        "  clear                             — saját objektumok törlése\n" +
        "  sort                              — gyűjtemény rendezése\n" +
        "  reorder                           — sorrend megfordítása\n" +
        "  remove_greater                    — nagyobb objektumok eltávolítása\n" +
        "  count_by_soundtrack_name <name>   — szám zeneszám szerint\n" +
        "  filter_contains_name <name>       — szűrés név szerint\n" +
        "  print_field_descending_impact_speed — sebesség csökkenő sorrendben\n" +
        "  execute_script <file>             — szkript végrehajtása"
    );
        hu.put("err.empty",            "A mező nem lehet üres");
        hu.put("err.long",             "Kérjük egész számot adjon meg");
        hu.put("err.double",           "Kérjük számot adjon meg (pl.: 10.5)");
        hu.put("err.speed_max",        "Az érték nem haladhatja meg a 597-et");
        hu.put("err.speed_neg",        "Az érték nem lehet negatív");
        hu.put("err.too_long",         "Az érték túl hosszú (max. 200 karakter)");
        hu.put("err.unexpected",       "Váratlan hiba");
        hu.put("err.validation_title", "Beviteli hiba");
        hu.put("err.server_null",      "A szerver nem válaszolt. Kérjük próbálja újra.");
        STRINGS.put("hu", hu);

        // --- АНГЛИЙСКИЙ (Канада) ---
        Map<String, String> en = new HashMap<>(ru);
        en.put("app.title",           "HumanBeing Collection");
        en.put("auth.title",          "Authentication");
        en.put("auth.login",          "Username");
        en.put("auth.password",       "Password");
        en.put("auth.btn.login",      "Log In");
        en.put("auth.btn.register",   "Register");
        en.put("auth.error.empty",    "Username and password cannot be empty");
        en.put("auth.error.conn",     "Cannot connect to server");
        en.put("main.tab.table",      "Table");
        en.put("main.tab.visual",     "Visualization");
        en.put("main.btn.add",        "Add");
        en.put("main.btn.edit",       "Edit");
        en.put("main.btn.delete",     "Delete");
        en.put("main.btn.refresh",    "Refresh");
        en.put("main.btn.clear",      "Clear (mine)");
        en.put("main.btn.sort",       "Sort");
        en.put("main.btn.reorder",    "Reorder");
        en.put("main.btn.info",       "Info");
        en.put("main.btn.help",       "Help");
        en.put("main.btn.logout",     "Log Out");
        en.put("main.btn.exit",       "Exit");
        en.put("main.filter.label",   "Filter:");
        en.put("main.filter.col",     "by column:");
        en.put("main.user.label",     "User:");
        en.put("main.lang.label",     "Language:");
        en.put("main.status.ready",   "Ready");
        en.put("main.status.loading", "Loading...");
        en.put("col.id",              "ID");
        en.put("col.name",            "Name");
        en.put("col.x",               "X");
        en.put("col.y",               "Y");
        en.put("col.created",         "Created");
        en.put("col.realHero",        "Real Hero");
        en.put("col.toothpick",       "Toothpick");
        en.put("col.speed",           "Impact Speed");
        en.put("col.soundtrack",      "Soundtrack");
        en.put("col.weapon",          "Weapon");
        en.put("col.mood",            "Mood");
        en.put("col.car",             "Car");
        en.put("col.owner",           "Owner");
        en.put("dlg.add.title",       "Add HumanBeing");
        en.put("dlg.edit.title",      "Edit HumanBeing");
        en.put("dlg.btn.save",        "Save");
        en.put("dlg.btn.cancel",      "Cancel");
        en.put("dlg.field.name",      "Name *");
        en.put("dlg.field.x",         "Coordinate X *");
        en.put("dlg.field.y",         "Coordinate Y *");
        en.put("dlg.field.realHero",  "Real Hero");
        en.put("dlg.field.toothpick", "Has Toothpick");
        en.put("dlg.field.speed",     "Impact Speed (≤597) *");
        en.put("dlg.field.soundtrack","Soundtrack *");
        en.put("dlg.field.weapon",    "Weapon Type");
        en.put("dlg.field.mood",      "Mood");
        en.put("dlg.field.carName",   "Car Name");
        en.put("dlg.field.carCool",   "Cool Car");
        en.put("msg.deleted",         "Object deleted");
        en.put("msg.noselect",        "No object selected");
        en.put("msg.notowner",        "You are not the owner of this object");
        en.put("msg.confirm.delete",  "Delete selected object?");
        en.put("msg.confirm.title",   "Confirmation");
        en.put("msg.server.error",    "Server error");
        en.put("cmd.count_soundtrack","Filter by soundtrack:");
        en.put("cmd.filter_name",     "Filter by name (contains):");
        en.put("cmd.remove_greater",  "Remove greater by speed:");
        en.put("cmd.dialog.title",    "Execute command");
        en.put("cmd.dialog.input",    "Enter argument:");
        en.put("vis.click.info",      "Click on an object for information");
        en.put("main.sort.label",   "Sort:");
        en.put("vis.control.hint",  "RMB+drag=pan  Scroll=zoom  LMB=select");
        en.put("help.text",
        "Available commands:\n" +
        "  help                              — show help\n" +
        "  info                              — collection information\n" +
        "  show                              — show all objects\n" +
        "  add                               — add new object\n" +
        "  update <id>                       — update object by ID\n" +
        "  remove_by_id <id>                 — remove object by ID\n" +
        "  clear                             — clear your objects\n" +
        "  sort                              — sort the collection\n" +
        "  reorder                           — reverse the order\n" +
        "  remove_greater                    — remove objects greater than given\n" +
        "  count_by_soundtrack_name <name>   — count by soundtrack name\n" +
        "  filter_contains_name <name>       — filter by name\n" +
        "  print_field_descending_impact_speed — speed descending\n" +
        "  execute_script <file>             — execute script"
    );
        en.put("err.empty",            "field cannot be empty");
        en.put("err.long",             "please enter a whole number");
        en.put("err.double",           "please enter a number (e.g.: 10.5)");
        en.put("err.speed_max",        "value cannot exceed 597");
        en.put("err.speed_neg",        "value cannot be negative");
        en.put("err.too_long",         "value too long (max 200 characters)");
        en.put("err.unexpected",       "Unexpected error");
        en.put("err.validation_title", "Input Error");
        en.put("err.server_null",      "Server did not respond. Please try again.");
        STRINGS.put("en", en);
    }

    public static Locale getCurrentLocale() { return currentLocale; }

    public static void setLocale(Locale locale) {
        currentLocale = locale;
        applyUIManagerLocale(locale);
        listeners.forEach(Runnable::run);
    }

    private static void applyUIManagerLocale(Locale locale) {
        if (locale.equals(RUSSIAN)) {
            UIManager.put("OptionPane.yesButtonText",    "Да");
            UIManager.put("OptionPane.noButtonText",     "Нет");
            UIManager.put("OptionPane.okButtonText",     "ОК");
            UIManager.put("OptionPane.cancelButtonText", "Отмена");
        } else if (locale.equals(GERMAN)) {
            UIManager.put("OptionPane.yesButtonText",    "Ja");
            UIManager.put("OptionPane.noButtonText",     "Nein");
            UIManager.put("OptionPane.okButtonText",     "OK");
            UIManager.put("OptionPane.cancelButtonText", "Abbrechen");
        } else if (locale.equals(HUNGARIAN)) {
            UIManager.put("OptionPane.yesButtonText",    "Igen");
            UIManager.put("OptionPane.noButtonText",     "Nem");
            UIManager.put("OptionPane.okButtonText",     "OK");
            UIManager.put("OptionPane.cancelButtonText", "Mégse");
        } else {
            UIManager.put("OptionPane.yesButtonText",    "Yes");
            UIManager.put("OptionPane.noButtonText",     "No");
            UIManager.put("OptionPane.okButtonText",     "OK");
            UIManager.put("OptionPane.cancelButtonText", "Cancel");
        }
    }

    public static void addListener(Runnable r) { listeners.add(r); }

    public static void removeListener(Runnable r) { listeners.remove(r); }

    public static String get(String key) {
        Map<String, String> map = STRINGS.get(currentLocale.getLanguage());
        if (map == null) map = STRINGS.get("en");
        String val = map.get(key);
        return val != null ? val : key;
    }

    public static String formatNumber(double value) {
        return NumberFormat.getNumberInstance(currentLocale).format(value);
    }

    public static String formatDate(LocalDateTime dt) {
        if (dt == null) return "";
        DateTimeFormatter fmt = DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.SHORT)
                .withLocale(currentLocale);
        return dt.format(fmt);
    }

    public static String getLocaleName(Locale locale) {
        if (locale.equals(RUSSIAN)) return "Русский";
        if (locale.equals(GERMAN)) return "Deutsch";
        if (locale.equals(HUNGARIAN)) return "Magyar";
        if (locale.equals(ENGLISH_CA)) return "English (CA)";
        return locale.getDisplayName();
    }
}
