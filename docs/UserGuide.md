---
layout: page
title: User Guide
---

DoctorWho is designed for clinic receptionists and administrative staff at small to mid-sized GP clinics. 
This guide assumes you are comfortable using a computer for everyday tasks, but no programming or technical experience is required.

Do you prefer typing information to having to click on multiple things? DoctorWho is for you!

DoctorWho is a **desktop app for managing patient information and appointments, optimized for use via a Command Line
Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, DoctorWho
can get your patient management tasks done faster than traditional GUI apps.

<h2 style="margin-top: 0;">Can DoctorWho help you?</h2>

Yes, if your clinic is still:
- Using paper filing to keep track of your patients and their appointments.
- Dealing with long search times in Excel.
- Paying exorbitant fees for enterprise grade software to track a handful of patients.

--------------------------------------------------------------------------------------------------------------------

<h2 style="margin-top: 0;">Table of Contents</h2>

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

Don't worry if you're not tech-savvy — just follow these steps one by one and you'll be up and running in no time!

1. First, make sure you have Java `17` or above installed on your computer. Not sure if you have it? Open a terminal and type `java -version` — if you see a version number of 17 or higher, you're good to go!<br>
   **Mac users:** Check out [this guide](https://se-education.org/guides/tutorials/javaInstallationMac.html) to get the exact JDK version you need.

2. Next, download the latest `doctorwho.jar` file from [here](https://github.com/AY2526S2-CS2103T-F10-1/tp/releases). You'll find it under the **Assets** section of the latest release — just click on `doctorwho.jar` to download it.

3. Move the `doctorwho.jar` file to the folder where you'd like to store your patient data. We recommend creating a brand new empty folder for this (e.g., a folder named `DoctorWho` on your Desktop), so everything stays neat and tidy.

   ![Moving .jar file to folder](images/ShiftingJarFile.png)

4. Now, let's launch the app! Open a command terminal in your `DoctorWho` folder:

   - **Windows:** Press `Win + R`, type `cmd`, and hit Enter.
   - **Mac/Linux:** Search for **Terminal** in Spotlight or your app menu.

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
   * `add n/John Doe ic/S1234567D x/M dob/01-04-2003 p/98765432 e/johnd@example.com a/John street, block 123, #01-01` — Adds a patient named `John Doe`.
   * `delete 3` — Deletes the 3rd patient in the current list.
   * `apt 3 d/01-04-2026 09:00 dur/60 note/Follow-up for diabetes review` — Schedules an appointment for the 3rd patient.
   * `lsapt d/12-03-2026` — List appointments for 12th March 2026.
   * `dapt 3` — Removes the appointment from the 3rd patient.

7. When you're ready to explore more, check out the [Features](#features) section for the full command details, or jump to the [Command Summary](#command-summary) for a quick cheatsheet!

--------------------------------------------------------------------------------------------------------------------

## Command Summary

Here is a quick reference list for the commands DoctorWho provides, more detailed information about all of the commands
can be found in [Features](#features).

| Action                                                   | Format, Examples                                                                                                                                                                                                                                       |
|----------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [**Add**](#adding-a-patient-add)                         | `add n/NAME ic/NRIC x/SEX dob/DOB p/PHONE_NUMBER e/EMAIL a/ADDRESS [al/ALLERGY] [mc/CONDITION]…​` <br> e.g., `add n/James Ho ic/S1234567D x/M dob/01-04-2003 p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 al/dust c/allergic rhinitis` |
| [**List**](#listing-all-patients-list)                   | `list`                                                                                                                                                                                                                                                 |
| [**Edit**](#editing-a-patient-edit)                      | `edit PATIENT_NUMBER [n/NAME] [ic/NRIC] [x/SEX] [dob/DOB] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [al/ALLERGY] [mc/CONDITION]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`                                                                   |
| [**Find**](#locating-patients-by-name-find)              | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`                                                                                                                                                                                             |
| [**Delete**](#deleting-a-patient-delete)                 | `delete PATIENT_NUMBER`<br> e.g., `delete 3`                                                                                                                                                                                                           |
| [**Add appointment**](#adding-an-appointment-apt)        | `apt PATIENT_NUMBER d/DATETIME dur/DURATION [note/NOTE]`<br> e.g., `apt 2 d/01-04-2026 09:00 dur/60 note/Follow-up for diabetes review `                                                                                                               |
| [**Delete appointment**](#deleting-an-appointment-dapt)  | `dapt PATIENT_NUMBER`<br> e.g., `dapt 1`                                                                                                                                                                                                               |
| [**List appointments**](#listing-all-appointments-lsapt) | `lsapt [d/DATE]`<br> e.g., `lsapt`, `lsapt d/14-03-2026`                                                                                                                                                                                               |
| [**Clear**](#clearing-all-entries-clear)                 | `clear`                                                                                                                                                                                                                                                |
| [**Help**](#viewing-help-help)                           | `help`                                                                                                                                                                                                                                                 |
| [**Exit**](#exiting-the-program-exit)                    | `exit`                                                                                                                                                                                                                                                 |

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g., `n/NAME [al/ALLERGY]` can be used as `n/John Doe al/Aspirin` or as `n/John Doe`.
  E.g., `n/NAME [mc/CONDITION]` can be used as `n/Johnny mc/High BP` or as `n/Johnny`

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g., `[al/ALLERGY]…​` can be used as ` ` (i.e., 0 times), `al/Penicillin`, `al/Ibuprofen al/Aspirin` etc.

* Parameters can be in any order.<br>
  e.g., if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g., if the command specifies `help 123`, it will be interpreted as `help`.

* Date format is `dd-MM-yyyy` for `add` and `edit` commands, `dd-MM-yyyy HH:mm` for appointment commands.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.

</div>

### Viewing help: `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`

## DoctorWho Operations

### Adding a patient: `add`

Adds a patient to DoctorWho.

Format: `add n/NAME ic/NRIC x/SEX dob/DOB p/PHONE_NUMBER e/EMAIL a/ADDRESS [al/ALLERGY] [mc/CONDITION]…​`

<div markdown="block" class="alert alert-info">

**:information_source: Accepted name formats**<br>

DoctorWho currently accepts the following special characters in the patient's name:

| Character       | Valid example |
|-----------------|---------------|
| Hyphens (-)     | Mary-Jane     |
| Apostrophes (') | O'Brien       |
| Commas (,)      | Henry, Tan    |

However, there are other common special characters used in names that are not yet supported. These are some suggested
replacements you can use if you encounter these special characters:

| Character  | Invalid example | Suggested replacements (Valid) | Future support                                             |
|------------|-----------------|--------------------------------|------------------------------------------------------------|
| Diacritics | Jäger           | Jager                          | Not planned                                                |
| Slash (/)  | Ali s/o Ahmad   | Ali so Ahmad<br>Ali SO Ahmad   | [Planned](DeveloperGuide.md#appendix-planned-enhancements) |

**Note (NRIC validation):**<br>
DoctorWho prevents invalid NRIC/FIN entries. For both `add` and `edit`, the `ic/` value must contain a valid NRIC/FIN in the required format; otherwise, the command is rejected. <br/>
  DoctorWho uses the NRIC/FIN to check whether an entry already exists, so duplicate NRIC/FIN values are rejected, while multiple patients may share the same name.

**Sex:**<br/>
Limited to male or female values only; `x/` accepts `M` or `F` case-insensitively (for example, `x/M`, `x/F`, `x/m`, and `x/f` are valid), though edits are allowed.

</div>

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A patient can have any number of allergies or medical conditions (including 0)
</div>

Examples:

* `add n/John Doe ic/S1234567D x/M dob/01-04-2003 p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
* `add n/Betsy Crowe ic/S2345678H x/F dob/02-04-2003 e/bcrowe@example.com a/Newgate Prison p/1234567 al/Penicillin mc/cold`
* `add n/Tim Chal ic/S4567890C x/M dob/03-04-2003 e/betsycrowe@example.com a/Newgate Prison p/1234567 al/Morphine`

### Listing all patients: `list`

Shows a list of all patients in DoctorWho.

Format: `list`

### Editing a patient: `edit`

Edits an existing patient in DoctorWho.

Format: `edit PATIENT_NUMBER [n/NAME] [ic/NRIC] [x/SEX] [dob/DOB] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [al/ALLERGY] [mc/CONDITION]…​`

* Edits the patient at the specified `PATIENT_NUMBER`. The index refers to the index number shown in the displayed
  patient list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing conditions and allergies, the existing ones of the patient will be removed i.e., adding is not cumulative.
* You can remove all the patient’s allergies or medical conditions by typing `al/` or `mc/` respectively, without
  specifying anything after it.

Examples:

* `edit 1 x/F p/91234567 e/johndoe@example.com` Edits the sex, phone number and email address of the 1st patient to be `F`, `91234567`
  and `johndoe@example.com` respectively.
* `edit 2 n/Betsy Crower al/ mc/` Edits the name of the 2nd patient to be `Betsy Crower` and clears all existing
  allergies and medical conditions.

### Locating patients by name: `find`

Finds patients whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g., `hans` will match `Hans`
* The order of the keywords does not matter. e.g., `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g., `Han` will not match `Hans`
* Persons matching at least one keyword will be returned (i.e., `OR` search).
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

Adds an appointment to the patient identified by the index number used in the displayed patient list. Supply the start date and time, duration and an optional note.

Format: `apt PATIENT_NUMBER d/DATETIME dur/DURATION [note/NOTE]`

* Creates and adds an appointment for the patient at the specified `PATIENT_NUMBER`.
* The `PATIENT_NUMBER` **must be a positive integer** 1, 2, 3, …​
* The date and time must be in the format `dd-MM-yyyy HH:mm` e.g., `12-03-2026 14:00` refers to 12th March 2026, 14:00.
* The duration **must be a positive integer** in **minutes**.
* The note is optional.

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

### Exiting the program: `exit`

Exits the program.

Format: `exit`

## Storage

### Saving the data

DoctorWho data is saved in the hard disk automatically after any command that changes the data. There is no need to save
manually.

### Editing the data file

DoctorWho data is saved automatically as a JSON file `[JAR file location]/data/doctorwho.json`. Advanced users are
welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, DoctorWho will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the DoctorWho to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

### Archiving data files `[coming in v2.0]`

Automatic archiving is not yet supported. In the meantime, we recommend
periodically making a manual copy of your data file located at
`[JAR file location]/data/doctorwho.json` and storing it in a separate
folder as a backup.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only
   the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the
   application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut
   `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to
   manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: What happens if I accidentally delete a patient?<br>
**A**: Deletion is irreversible and the patient's data cannot be
recovered from within the app. To safeguard against accidental
deletion, we recommend periodically making a manual copy of your
data file located at `[JAR file location]/data/doctorwho.json`
and storing it in a separate folder. If you have a copy, you can
restore it by replacing the current data file with your backup.

--------------------------------------------------------------------------------------------------------------------

**Q**: Can I schedule multiple appointments for one patient?<br>
**A**: Currently, DoctorWho supports one active appointment per
patient at a time. To schedule a new appointment, use the `apt`
command which will replace the existing one. Support for multiple
appointments per patient is planned for a future version.

--------------------------------------------------------------------------------------------------------------------

**Q**: How do I transfer my data to another computer?<br>
**A**: Install the app on the other computer and overwrite the
empty data file it creates with the file from your previous
DoctorWho folder, located at `[JAR file location]/data/doctorwho.json`.

--------------------------------------------------------------------------------------------------------------------

## Glossary

* Allergy: A hypersensitivity to a substance that causes the body to react with an immune response.
* CLI: Command Line Interface. A text-based interface where users interact with a program by typing commands.
* Command: An instruction given to a computer program to perform a specific task.
* GUI: Graphical User Interface. A visual-based interface where users interact with a program by interacting with
  windows, icons and menus.
* Index: A number that refers to the position of a patient in the displayed list.
* Java: The programming language used to implement this application.
* JSON: JavaScript Object Notation. A lightweight, text-based data interchange format, easily parsable by machines.
* Keyword: A word or a phrase that is used to search for a patient.
* Medical Condition: A health problem that requires ongoing management.
* NRIC: National Registration Identity Card. A unique identification number assigned to citizens and permanent residents.
* Parameters: Inputs provided to a command. A command may have zero or more parameters depending on its functionality.
* Patient: A person receiving medical care whose information and appointments are managed within DoctorWho.
* UTF-8: The standard for how Unicode numbers are translated into binary numbers to be stored in the computer.
