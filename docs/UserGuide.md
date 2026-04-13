---
layout: page
title: User Guide
---

DoctorWho is designed for clinic receptionists and administrative staff at single-doctor General Practitioner (GP) clinics. 
This guide assumes you are comfortable using a computer for everyday tasks, but no programming or technical experience is required.

Do you prefer typing information to having to click on multiple things? DoctorWho is for you!

DoctorWho is a **desktop app for managing patient information and appointments, optimized for use via a Command Line
Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, DoctorWho
can get your patient management tasks done faster than traditional GUI apps.

<h2 style="margin-top: 0;">Can DoctorWho help you?</h2>

Yes, if your clinic is still:
- Using paper filing to keep track of your patients and their appointments.
- Dealing with long search times in Excel.
- Paying exorbitant fees for enterprise grade software to only track a handful of patients.

--------------------------------------------------------------------------------------------------------------------

<h2 style="margin-top: 0;">Table of Contents</h2>

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

Don't worry if you're not tech-savvy — just follow these steps one by one, and you'll be up and running in no time!

1. First, make sure you have Java `17` installed on your computer. Not sure if you have it? Open a terminal and type `java -version` — if you see the version number 17, you're good to go!<br>
   **macOS users:** Check out [this guide](https://se-education.org/guides/tutorials/javaInstallationMac.html) to get the exact JDK version you need.

2. Next, download the latest `doctorwho.jar` file from [here](https://github.com/AY2526S2-CS2103T-F10-1/tp/releases). You'll find it under the **Assets** section of the latest release — just click on `doctorwho.jar` to download it.

3. Move the `doctorwho.jar` file to the folder where you'd like to store your patient data. We recommend creating a brand-new empty folder for this (e.g., a folder named `DoctorWho` on your Desktop), so everything stays neat and tidy.

   ![Moving .jar file to folder](images/ShiftingJarFile.png)

4. Now, let's launch the app! Open a command terminal in your `DoctorWho` folder:

   - **Windows:** Press `Win + R`, type `cmd`, and hit Enter.
   - **macOS/Linux:** Search for **Terminal** in Spotlight or your app menu.

   Then, navigate to your folder. For example, if you placed it on your Desktop in a folder called `DoctorWho`, type:
    ```
    cd Desktop/DoctorWho
    ```
   Finally, run the app with:
    ```
    java -jar doctorwho.jar
    ```

   ![Opening the app via terminal](images/OpeningJarFile.png)

5. DoctorWho will launch with some sample data so you can explore right away — it should look something like this:

   ![DoctorWho UI](images/Ui.png)

6. Type a command in the command box at the top and press **Enter** to run it. Here are a few to try out:

   * `list` — Lists all patients.
   * `add n/John Doe ic/T0300001F x/M dob/01-04-2003 p/98765432 e/johnd@example.com a/John street, block 123, #01-01` — Adds a patient named `John Doe`.
   * `delete 3` — Deletes the 3rd patient in the current list.
   * `apt 3 d/01-04-2026 09:00 dur/60 note/Follow-up for diabetes review` — Schedules an appointment for the 3rd patient.
   * `lsapt d/12-03-2026` — Lists appointments for 12th March 2026.
   * `dapt 1` — Removes the appointment from the 1st patient.

7. When you're ready to explore more, check out the [Features](#features) section for the full command details, or jump to the [Command Summary](#command-summary) for a quick cheatsheet!

--------------------------------------------------------------------------------------------------------------------

## Command Summary

Here is a quick reference list for the commands DoctorWho provides, more detailed information about all of the commands
can be found in [Features](#features).

| Action                                                   | Format, Examples                                                                                                                                                                                                                                         |
|----------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [**Add**](#adding-a-patient-add)                         | `add n/NAME ic/NRIC x/SEX dob/DOB p/PHONE_NUMBER e/EMAIL a/ADDRESS [al/ALLERGY]…​ [mc/CONDITION]…​`<br> e.g., `add n/James Ho ic/T0300002D x/M dob/01-04-2003 p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 al/dust mc/allergic rhinitis` |
| [**List**](#listing-all-patients-list)                   | `list`                                                                                                                                                                                                                                                   |
| [**Edit**](#editing-a-patient-edit)                      | `edit PATIENT_NUMBER [n/NAME] [ic/NRIC] [x/SEX] [dob/DOB] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [al/ALLERGY]…​ [mc/CONDITION]…​`<br> e.g., `edit 2 n/James Lee e/jameslee@example.com`                                                                  |
| [**Find**](#locating-patients-by-name-find)              | `find KEYWORD [MORE_KEYWORDS]…​`<br> e.g., `find James Jake`                                                                                                                                                                                             |
| [**Delete**](#deleting-a-patient-delete)                 | `delete PATIENT_NUMBER`<br> e.g., `delete 3`                                                                                                                                                                                                             |
| [**Add appointment**](#adding-an-appointment-apt)        | `apt PATIENT_NUMBER d/DATETIME dur/DURATION [note/NOTE]`<br> e.g., `apt 2 d/01-04-2026 09:00 dur/60 note/Follow-up for diabetes review `                                                                                                                 |
| [**Delete appointment**](#deleting-an-appointment-dapt)  | `dapt PATIENT_NUMBER`<br> e.g., `dapt 1`                                                                                                                                                                                                                 |
| [**List appointments**](#listing-all-appointments-lsapt) | `lsapt [d/DATE]`<br> e.g., `lsapt`, `lsapt d/14-03-2026`                                                                                                                                                                                                 |
| [**Clear**](#clearing-all-entries-clear)                 | `clear`                                                                                                                                                                                                                                                  |
| [**Help**](#viewing-help-help)                           | `help`                                                                                                                                                                                                                                                   |
| [**Exit**](#exiting-the-program-exit)                    | `exit`                                                                                                                                                                                                                                                   |

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g., in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g., `n/NAME [al/ALLERGY]` can be used as `n/John Doe al/Aspirin` or as `n/John Doe`.
  e.g., `n/NAME [mc/CONDITION]` can be used as `n/Johnny mc/High BP` or as `n/Johnny`

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g., `[al/ALLERGY]…​` can be used as `​ ​` (i.e., 0 times), `al/Penicillin`, `al/Ibuprofen al/Aspirin` etc.

* Parameters can be in any order.<br>
  e.g., if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Prefixes can be surrounded by any number of spaces, these excess spaces will be trimmed from the input.<br>
  e.g., `add     n/NAME    ic/NRIC` and `add n/     NAME ic/NRIC`, will be interpreted as `add n/NAME ic/NRIC`.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit`, and `clear`) will be ignored.<br>
  e.g., if the command specifies `help 123`, it will be interpreted as `help`.

* Date format is `dd-MM-yyyy` for `add` and `edit` commands, `dd-MM-yyyy HH:mm` for appointment commands.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application. 

* Note that the zero-width space(ZWSP) character is used in some examples showing consecutive spaces in an effort to fix HTML collapsing the whitespace. Therefore, you may face some issues directly copying and pasting those examples as the ZWSP is considered an invalid character for most parameters, we recommend typing out those examples.

</div>

## DoctorWho Operations

### Adding a patient: `add`

Adds a patient to DoctorWho.

Format: `add n/NAME ic/NRIC x/SEX dob/DOB p/PHONE_NUMBER e/EMAIL a/ADDRESS [al/ALLERGY]…​ [mc/CONDITION]…​`

* Will fail if there is an existing patient with the same NRIC, however other fields allow duplicates i.e., two patients may have the same name.
* The date of birth(dob) must be in the format `dd-MM-yyyy` and must be either the current date or earlier e.g., `12-03-2026` refers to 12th March 2026.
* All parameters are compulsory, except for those in square brackets.

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A patient can have any number of allergies or medical conditions (including 0)
</div>

Examples:

* `add n/John Doe ic/T0300003B x/M dob/01-04-2003 p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
* `add n/Betsy Crowe ic/T0300004J x/F dob/02-04-2003 e/bcrowe@example.com a/Newgate Prison p/1234567 al/Penicillin mc/cold`
* `add n/Tim Chal ic/T0300005I x/M dob/03-04-2003 e/betsycrowe@example.com a/Newgate Prison p/1234567 al/Morphine`

### Listing all patients: `list`

Shows a list of all patients in DoctorWho.

Format: `list`

### Editing a patient: `edit`

Edits an existing patient in DoctorWho.

Format: `edit PATIENT_NUMBER [n/NAME] [ic/NRIC] [x/SEX] [dob/DOB] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [al/ALLERGY]…​ [mc/CONDITION]…​`

* Edits the patient at the specified `PATIENT_NUMBER`. The index refers to the index number shown in the displayed
  patient list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing conditions and allergies, the existing ones of the patient will be removed i.e., adding is not cumulative.
* You can remove all the patient’s allergies or medical conditions by typing `al/` or `mc/` respectively, without
  any other entries.<br>
  e.g., `al/ al/Ibuprofen` and `mc/IBS mc/` will not work.

Examples:

* `edit 1 x/F p/91234567 e/johndoe@example.com` Edits the sex, phone number, and email address of the 1st patient to be `F`, `91234567`,
  and `johndoe@example.com` respectively.
* `edit 2 n/Betsy Crower al/ mc/` Edits the name of the 2nd patient to be `Betsy Crower` and clears all existing
  allergies and medical conditions.

### Locating patients by name: `find`

Finds patients whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]…​`

* The search is case-insensitive. e.g., `hans` will match `Hans`
* The order of the keywords does not matter. e.g., `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g., `Han` will not match `Hans`
* Patients matching at least one keyword will be returned (i.e., OR search).
  e.g., `Hans Bo` will return `Hans Gruber`, `Bo Yang`

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
Use `find` before `delete` or `edit` to locate the right patient 
first. This reduces the risk of accidentally modifying the wrong 
patient's records.
</div>

Examples:

* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Deleting a patient: `delete`

Deletes the specified patient from DoctorWho.

Format: `delete PATIENT_NUMBER`

* Deletes the patient at the specified `PATIENT_NUMBER`.
* The PATIENT_NUMBER **must be a positive integer** 1, 2, 3, …​

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
This action is irreversible. Once a patient is deleted, all their 
information and appointments cannot be recovered. Use `find` first 
to confirm you have the right patient before deleting.
</div>

Examples:

* `list` followed by `delete 2` deletes the 2nd patient in DoctorWho.
* `find Betsy` followed by `delete 1` deletes the 1st patient in the results of the `find` command.

### Adding an appointment: `apt`

Adds an appointment to the patient identified by the index number used in the displayed patient list. Supply the start date and time, duration, and an optional note.

Format: `apt PATIENT_NUMBER d/DATETIME dur/DURATION [note/NOTE]`

* Creates and adds an appointment for the patient at the specified `PATIENT_NUMBER`.
* The `PATIENT_NUMBER` **must be a positive integer** 1, 2, 3, …​
* The date and time must be in the format `dd-MM-yyyy HH:mm` e.g., `12-03-2026 14:00` refers to 12th March 2026, 14:00.
* The duration **must be between 1 and 600 minutes inclusive**.
* The note is optional.
* If provided, `NOTE` must be at most **500 characters**.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If the patient already has an existing appointment, it will be 
silently overwritten with no confirmation prompt. Check the 
patient's current appointment before using this command.
</div>

Examples:

* `apt 1 d/12-03-2026 14:00 dur/30 note/Routine Checkup` adds an appointment to the 1st patient, from the
  top, of the patient list, scheduled for 12th March 2026, at 14:00. A note will be indicated with
  `Note | Routine Checkup`

![img.png](images/aptOutput.png)

### Deleting an appointment: `dapt`

Deletes the appointment of the patient identified by the index number used in the displayed patient list.

Format: `dapt PATIENT_NUMBER`

* Deletes the appointment for the patient at the specified `PATIENT_NUMBER`.
* The `PATIENT_NUMBER` refers to the index number shown in the displayed patient list.
* The `PATIENT_NUMBER` **must be a positive integer** 1, 2, 3, …​

Examples:

* `list` followed by `dapt 1` deletes the appointment for the 1st patient in the displayed patient list.

### Listing all appointments: `lsapt`

Shows all appointments across all patients, sorted by date-time in ascending order, to give a daily schedule view.

Format: `lsapt [d/DATE]`

* Lists all scheduled appointments when no date is provided.
* If `d/DATE` is provided, only appointments on that date will be shown, and listed from earliest to latest.
* `DATE` must be in the format `dd-MM-yyyy` e.g., `14-03-2026` refers to 14th March 2026.

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
Run `lsapt d/TODAY'S DATE` at the start of your shift to get a 
quick overview of all appointments for the day, sorted from 
earliest to latest.
</div>

Examples:

* `lsapt` returns all appointments across all patients, sorted by date-time ascending.
* `lsapt d/14-03-2026` returns all appointments on 14th March 2026.

![img.png](images/lsaptOutput.png)

### Clearing all entries: `clear`

Clears all entries from DoctorWho.

Format: `clear`

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
This action is irreversible and will delete all patient data.
</div>

### Viewing help: `help`

Opens a window that provides a summary of available commands and explains how to access the help page. (Pressing the `F1` key will also open this window)

<img src="images/helpMessage.png" width="750" alt="Help Window">

Format: `help`

### Exiting the program: `exit`

Exits the program.

Format: `exit`

### Command parameters

<div markdown="block" class="alert alert-info">

**:information_source: Additional details on accepted parameter values:**<br>

#### For the `add` and `edit` commands:

**Name (`n/`):**<br>
Must be **1 to 100 characters** long and start and end with an alphanumeric character.
DoctorWho currently accepts the following special characters in the patient's name:<br>

| Character       | Valid example |
|-----------------|---------------|
| Hyphens (-)     | Mary-Jane     |
| Apostrophes (') | O'Brien       |
| Commas (,)      | Henry, Tan    |

Examples of invalid name values: `n/-David`, `n/'Aisha`, `n/,Tan`, `n/David-`, `n/O''Brien`, `n/asdj- -sads`.

However, names should not start or end with these special characters or space.<br>

There are other common special characters used in names that are not yet supported. This is a non-exhaustive list of some suggested
replacements you can use if you encounter these special characters:

| Character  | Invalid example | Suggested replacements (Valid) | Future support                                             |
|------------|-----------------|--------------------------------|------------------------------------------------------------|
| Diacritics | Jäger           | Jager                          | Not planned                                                |
| Slash (/)  | Ali s/o Ahmad   | Ali so Ahmad<br>Ali SO Ahmad   | [Planned](DeveloperGuide.md#appendix-planned-enhancements) |

**NRIC (`ic/`):**<br>
DoctorWho prevents invalid NRIC/FIN entries. The `ic/` value must contain a valid NRIC/FIN in the required format; otherwise, the command is rejected.<br>
* More information on the NRIC/FIN validation can be found in the [Developer Guide](DeveloperGuide.md#nric-validation-feature).

**Sex (`x/`):**<br>
Limited to male or female values only; `x/` accepts `M` or `F` case-insensitively (for example, `x/M`, `x/F`, `x/m`, and `x/f` are valid), though edits are allowed.

**Phone number (`p/`):**<br>
Must contain only digits and be between **3 to 15 digits** long (inclusive).<br/>
Examples of valid values: `p/123`, `p/98765432`, `p/651234567890123`.<br>
Examples of invalid values: `p/+6598765432`, `p/12`, `p/123-4567`.

**Email (`e/`):**<br>
Must be of the format `local-part@domain` and adhere to the following constraints:
1. The local-part should only contain alphanumeric characters and these special characters, excluding the parentheses, (+_.-). The local-part may not start or end with any special characters.
2. This is followed by a '@' and then a domain name. The domain name is made up of domain labels separated by periods.
   The domain name must:
* end with a domain label at least 2 characters long
* have each domain label start and end with alphanumeric characters
* have each domain label consist of alphanumeric characters, separated only by hyphens, if any.

Examples of valid values: `e/alex.tan+clinic@example.com`, `e/a_b-c@sub-domain.example`.<br>
Examples of invalid values: `e/.alex@example.com`, `e/alex@-example.com`, `e/alex@example.c`.

In the case that the user is unable to provide an email address, DoctorWho accepts a dead-end email address such as, `user@example.com`.

**Address (`a/`):**<br/>
Must be **1 to 200 characters** long and not blank after trimming leading/trailing spaces.<br/>
It may contain only alphanumeric characters, single spaces between words, and these special characters: `#`, `.`, `,`, `(`, `)`, `\`, `-`.<br/>
Examples of valid values: `a/123 Clementi Rd`, `a/Blk 123, #01-01 (Tower A)`, `a/Jurong\West Ave 2`.<br/>
Examples of invalid values: `a/`, `a/   `, `a/Blk  123` (double spaces), `a/Bedok@Ave` (`@` not allowed).

**Drug Allergies (`al/`):**<br/>
Must be **1 to 30 characters** long and contain only alphanumeric characters, single spaces between words, and single hyphens within a word.<br/>
Examples of valid values: `al/Penicillin`, `al/Beta-lactam`, `al/Type 2`.<br/>
Examples of invalid values: `al/Beta--lactam` (consecutive hyphens), `al/Beta- lactam` (space after hyphen), `al/Aspirin!` (`!` not allowed).

The parser also conducts a case-sensitive duplicate check so, `mc/penicillin mc/penicillin` will create one `penicillin` medical condition, but `al/penicillin al/Penicillin` will create both `penicillin` and `Penicillin`.

**Medical Conditions (`mc/`):**<br/>
Must be **1 to 50 characters** long and contain only alphanumeric characters, single spaces between words, and single hyphens within a word.<br/>
Examples of valid values: `mc/Type 2 diabetes`, `mc/Post-traumatic stress disorder`, `mc/High BP`.<br/>
Examples of invalid values: `mc/Post--traumatic stress disorder` (consecutive hyphens), `mc/Type ​ 2 ​ diabetes` (consecutive spaces), `mc/Diabetes?` (`?` not allowed).

The parser also conducts a case-sensitive duplicate check so, `mc/diabetes mc/diabetes` will create one `diabetes` medical condition, but `mc/diabetes mc/Diabetes` will create both `diabetes` and `Diabetes`.

#### For the `apt` command:

**Note (`note/`):**<br>
Is limited to 500 characters and can include any character that you can type on your keyboard, except the `/` character, we recommend replacing it with the `\` or `|` characters.

</div>

## Storage

### Saving the data

DoctorWho data is saved in the hard disk automatically after any command that changes the data. There is no need to save
manually.

### Editing the data file

DoctorWho data is saved automatically as a JSON file `[JAR file location]/data/doctorwho.json`. Advanced users are
welcome to update data directly by editing that data file, which follows the following format.

```json
{
  "patients" : [ {
    "name" : "Alex Yeoh",
    "nric" : "S1234567D",
    "sex" : "F",
    "dob" : "01-04-2003",
    "phone" : "87438807",
    "email" : "alexyeoh@example.com",
    "address" : "Blk 30 Geylang Street 29, #06-40",
    "tags" : [ "allergy:Penicillin", "condition:Diabetes" ],
    "appointmentStart" : "12-03-2026 14:00",
    "appointmentDuration" : 30,
    "appointmentNote" : "Initial Consultation"
  }, {
    "name" : "Bernice Yu",
    "nric" : "S2345678H",
    "sex" : "F",
    "dob" : "02-04-2003",
    "phone" : "99272758",
    "email" : "berniceyu@example.com",
    "address" : "Blk 30 Lorong 3 Serangoon Gardens, #07-18",
    "tags" : [ "condition:Hypertension", "allergy:Aspirin" ],
    "appointmentStart" : null,
    "appointmentDuration" : null,
    "appointmentNote" : null
  } ]
}
```

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, DoctorWho will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause DoctorWho to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: What happens if I accidentally delete a patient?<br>
**A**: When a patient is deleted, their details are shown in the GUI, therefore it is possible to re-add the patient before another command is run. Subsequently, deletion is irreversible and the patient's data cannot be recovered from within the app. To further safeguard against accidental deletion, we recommend periodically making a manual copy of your data file located at `[JAR file location]/data/doctorwho.json` and storing it in a separate folder. If you have a copy, you can restore it by replacing the current data file with your backup.

--------------------------------------------------------------------------------------------------------------------

**Q**: Can I schedule multiple appointments for one patient?<br>
**A**: Currently, DoctorWho supports one active appointment per patient at a time. To schedule a new appointment, use the `apt` command which will replace the existing one. Support for multiple appointments per patient is planned for a future version.

--------------------------------------------------------------------------------------------------------------------

**Q**: How do I transfer my data to another computer?<br>
**A**: Install the app on the other computer and overwrite the empty data file it creates with the file from your previous DoctorWho folder, located at `[JAR file location]/data/doctorwho.json`.

--------------------------------------------------------------------------------------------------------------------

## Glossary

* CLI: Command Line Interface. A text-based interface where users interact with a program by typing commands.
* Command: An instruction given to a computer program to perform a specific task.
* GUI: Graphical User Interface. A visual-based interface where users interact with a program by interacting with
  windows, icons and menus.
* JSON: JavaScript Object Notation. A lightweight, text-based data interchange format, easily parsable by machines.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only
   the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the
   application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut
   `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to
   manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Final word
Thank you once again for using **DoctorWho**! We hope that you will find our product beneficial to managing your patients seamlessly! For your reference here is a link to the [Command Summary](#command-summary). Should you have any queries, please do not hesitate to reach out to any of us! Our details can be found in our [About Us Page](AboutUs.md).
