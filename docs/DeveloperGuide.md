---
layout: page
title: Developer Guide
---

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* AddressBook-Level3 (AB3): The original source code for this application was adapted from the
[AddressBook-Level3](https://github.com/se-edu/addressbook-level3)
 project created by the SE-EDU initiative.
* NRIC Checksum: Introduce documentation for NRIC checksum. [Link](https://userapps.support.sap.com/sap/support/knowledge/en/2572734)
* JavaFX: Used for the Graphical User Interface (GUI). [Link](https://openjfx.io/)
* JUnit5: Used for the unit testing framework. [Link](https://junit.org/junit5/)
* PlantUML: Used to generate the diagrams in this documentation. [Link](https://plantuml.com/)
* Icons: PNG Icons from [ICONPACKS](https://www.iconpacks.net/)

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams are in this document `docs/diagrams` folder. Refer to the [
_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create
and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [
`Main`](https://github.com/AY2526S2-CS2103T-F10-1/tp/tree/master/src/main/java/doctorwho/Main.java) and [
`MainApp`](https://github.com/AY2526S2-CS2103T-F10-1/tp/tree/master/src/main/java/doctorwho/MainApp.java)) is in
charge of the app launch and shut down.

* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues
the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class which follows the corresponding API
  `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using
the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component
through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the
implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2526S2-CS2103T-F10-1/tp/tree/master/src/main/java/doctorwho/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PatientListPanel`,
`StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures
the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that
are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2526S2-CS2103T-F10-1/tp/tree/master/src/main/java/doctorwho/ui/MainWindow.java)
is specified in [`MainWindow.fxml`](https://github.com/AY2526S2-CS2103T-F10-1/tp/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Patient` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2526S2-CS2103T-F10-1/tp/tree/master/src/main/java/doctorwho/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API
call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates
   a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
2. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which
   is executed by the `LogicManager`.
3. The command can communicate with the `Model` when it is executed (e.g. to delete a patient).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take
   several interactions (between the command object and the `Model`) to achieve.
4. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:

* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a
  placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse
  the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a
  `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser`
  interface so that they can be treated similarly where possible e.g, during testing.

### Model component

**API** : [`Model.java`](https://github.com/AY2526S2-CS2103T-F10-1/tp/tree/master/src/main/java/doctorwho/model/Model.java)

<img src="images/ModelClassDiagram.png" width="800" />


The `Model` component,

* stores the address book data i.e., all `Patient` objects (which are contained in a `UniquePatientList` object).
* stores the currently 'selected' `Patient` objects (e.g., results of a search query) as a separate _filtered_ list
  which is exposed to outsiders as an unmodifiable `ObservableList<Patient>` that can be 'observed' e.g. the UI can be
  bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a
  `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they
  should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Patient` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Patient` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="700" />

</div>

### Storage component

**API** : [`Storage.java`](https://github.com/AY2526S2-CS2103T-F10-1/tp/tree/master/src/main/java/doctorwho/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,

* can save both address book data and user preference data in JSON format, and read them back into corresponding
  objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only
  the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects
  that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `doctorwho.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### NRIC validation feature

#### Context

NRIC/FIN is a core patient identifier and must be validated strictly. The app validates NRIC in two layers:

1. **Structure check**: first letter + seven digits + checksum letter.
2. **Checksum check**: Singapore NRIC/FIN modulus-11 checksum, including prefix-specific letter tables.

This prevents malformed or checksum-invalid NRIC values from entering the model.

#### Where validation happens

Validation is centralized in `Nric` (model), instead of being duplicated in parser/storage:

* `AddCommandParser` and `EditCommandParser` parse the `ic/` value and construct `Nric`.
* `JsonAdaptedPatient` also constructs `Nric` during JSON deserialization.
* `Nric#isValidNric(String)` is therefore the single source of truth for all input paths.

This design guarantees consistent behavior for CLI input, test fixtures, and persisted data loading.

#### Checksum algorithm

For an NRIC/FIN value with prefix `P`, digits `d1..d7`, and suffix letter `L`:

1. Compute weighted sum using weights `[2, 7, 6, 5, 4, 3, 2]`.
2. Add prefix offset:
   * `+4` for `T` and `G`
   * `+3` for `M`
   * `+0` for `S` and `F`
3. Compute `remainder = sum mod 11`.
4. Compute `checkDigit = 11 - (remainder + 1)`.
5. Map `checkDigit` to letter table based on prefix group:
   * `S/T -> ABCDEFGHIZJ`
   * `F/G -> KLMNPQRTUWX`
   * `M   -> KLJNPQRTUWX`

NRIC is valid only if computed suffix letter equals `L`.

#### Design considerations

**Aspect: location of checksum logic**

* **Alternative 1 (chosen):** keep checksum logic in `Nric`.
  * Pros: one validation implementation across parser, model, and storage.
  * Cons: parser tests must use checksum-valid NRIC fixtures.

* **Alternative 2:** validate in parser only.
  * Pros: simpler parser flow.
  * Cons: invalid values could still enter through storage or future non-parser paths.

#### Tests

NRIC behavior is covered by:

* `NricTest`: constructor guardrails, normalization, format checks, checksum-valid and checksum-invalid cases.
* `AddCommandParserTest`: invalid NRIC parsing failures.
* `JsonAdaptedPatientTest`: invalid NRIC in JSON rejected during conversion.

In addition, shared test fixtures (e.g., `TypicalPatients`, `PatientBuilder`) use checksum-valid NRIC values to avoid false failures.

**Additional checksum-valid NRIC test values (You may copy and use these):**

```text
T0314597I
T0314598G

S1234567D
S1490542A

F9340704X
F8164046R

G3866432N
G7296071N

M8635947W
M3594121L
```

### \[Proposed\] Automated Appointment Reminders

The proposed appointment reminder feature will alert the doctor of any upcoming appointments within the next 24 hours upon launching the application or while it is running.

#### Proposed Implementation

* A `ReminderManager` class will be added to the `Logic` component.
* `ReminderManager` will periodically query the `Model` for patients with an `Appointment` whose start time falls within a specific threshold (e.g., next 24 hours).
* The `UI` will be updated to include a `ReminderPanel` that observes the `ReminderManager` and displays upcoming appointments in a dedicated side panel or via visual indicators next to patient names.

#### Design considerations

**Aspect: trigger mechanism for reminder refresh**

* **Alternative 1 (chosen):** hybrid trigger (`Model` change events + periodic refresh).
  * Pros: reminders stay up to date after command execution and remain accurate as time passes.
  * Pros: avoids heavy continuous polling while app is idle.
  * Cons: requires careful scheduling/lifecycle management to avoid duplicate refreshes.

* **Alternative 2:** fixed-interval polling only.
  * Pros: simpler implementation model.
  * Cons: can introduce stale reminders between poll intervals or unnecessary background work.

**Aspect: reminder presentation strategy**

* **Alternative 1 (chosen):** non-blocking reminder panel with optional visual markers.
  * Pros: supports continuous workflow without forcing modal interactions.
  * Pros: allows users to review reminders alongside patient data.
  * Cons: reminders may be less noticeable if users ignore side panels.

* **Alternative 2:** modal pop-up alerts.
  * Pros: high visibility for urgent reminders.
  * Cons: interrupts command flow and can be intrusive when multiple reminders are due.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* has a need to manage a significant number of patient contacts, and appointments.
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps
* may vary in technological confidence but prefers efficient keyboard-driven interaction

**Value proposition**: manage patient details like chronic conditions, severe allergies, and appointment scheduling
faster than a typical mouse/GUI driven app

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority  | As an ...   | I want to ...                                                                          | So that I can ...                                                     |
|:----------|:------------|:---------------------------------------------------------------------------------------|:----------------------------------------------------------------------|
| `* * *`   | Admin Staff | add a patient's medical condition                                                      | provide informed care                                                 |
| `* * *`   | Admin Staff | add an appointment date to a patient                                                   | track my daily schedule                                               |
| `* * *`   | Admin Staff | delete a patient record                                                                | keep my database clean of inactive patients                           |
| `* * *`   | Admin Staff | have a data file automatically created on first launch                                 | start using the system without manual setup                           |
| `* * *`   | Admin Staff | see sample patient data on first launch                                                | understand what the app looks like in use                             |
| ` * * * ` | Admin Staff | list all patients                                                                      | see all my patients at a glance                                       |
| ` * * * ` | Admin Staff | find a patient by name                                                                 | quickly locate a specific patient's record                            |
| ` * * * ` | Admin Staff | delete a patient's appointment                                                         | remove outdated or cancelled appointments                             |
| `* * *`   | Admin Staff | list all appointments                                                                  | view my full schedule at a glance                                     |
| `* * *`   | Admin Staff | filter appointments by a specific date                                                 | see my schedule for that day                                          |
| `* * *`   | Admin Staff | add a patient's allergy                                                                | avoid prescribing harmful medication                                  |
| `* * *`   | Admin Staff | add a new patient record                                                               | keep track of new patients                                            |
| `* * *`   | Admin Staff | have my data automatically loaded on startup                                           | continue work across sessions                                         |
| `* * *`   | Admin Staff | receive a clear error message and correction technique when I enter an invalid command | fix my command                                                        |
| `* * *`   | Admin Staff | want the program to work immediately after opening                                     | don't have to install or configure anything                           |
| `* * *`   | Admin Staff | access the user guide via the help command                                             | know what actions are possible                                        |
| `* * *`   | Admin Staff | edit an existing patient's record                                                      | keep my database updated to the newest information                    |
| `* * *`   | Admin Staff | be alerted if I book two appointments at the same time                                 | avoid double-booking myself                                           |
| `* * *`   | Admin Staff | be told when a search returns no results                                               | know the system is working correctly                                  |
| `* * *`   | Admin Staff | exit the application                                                                   | close the app when done                                               |
| `* * *`   | Admin Staff | clear all patient records                                                              | start fresh with a clean database                                     |
| `* * *`   | Admin Staff | add notes to an appointment                                                            | remember important details for the visit                              |
| `* *`     | Admin Staff | tag a patient with 'High Risk'                                                         | am extra cautious when reviewing their file                           |
| `* *`     | Admin Staff | mark an allergy as "Severe"                                                            | it stands out visually when I open the patient profile                |
| `* *`     | Admin Staff | be warned before permanently deleting a patient record                                 | don't lose data accidentally                                          |
| `* *`     | Admin Staff | input command arguments in any order                                                   | don't have to memorize rigid syntax                                   |
| `* *`     | Admin Staff | record a patient's blood type                                                          | provide it quickly in an emergency                                    |
| `*`       | Admin Staff | list all patients with a specific allergy                                              | avoid prescribing dangerous medication during an outbreak or shortage |
| `*`       | Admin Staff | search for a patient by a partial or misspelled name                                   | find records quickly even if I don't remember the exact spelling      |
| `*`       | Admin Staff | use command aliases (e.g., a for add)                                                  | minimize typing time while talking to a patient                       |
| `*`       | Admin Staff | list all patients taking a specific medication                                         | contact them if that drug is recalled                                 |
| `*`       | Admin Staff | add a "Next Checkup" date                                                              | follow up on chronic condition progress                               |
| `*`       | Admin Staff | scrub "soft deleted" data permanently                                                  | comply with "right to be forgotten" regulations                       |
| `*`       | Admin Staff | link related patients                                                                  | review hereditary patterns                                            |
| `*`       | Admin Staff | attach external specialist notes                                                       | have a full care picture                                              |
| `*`       | Admin Staff | chain commands together                                                                | add a patient and their first appointment in one line                 |

### Use cases

(For all use cases below, the **System** is `DoctorWho` and the **Actor** is the `Staff`, unless specified otherwise)

**Use Case 01: Add a Patient**

**Preconditions:**
* Staff has launched the DoctorWho application.
* Staff is at the command prompt.

**Main Success Scenario:**
1. Staff requests to add a new patient with the required details.
2. DoctorWho adds the patient to the system.
3. DoctorWho shows a success message with the added patient's details.

   Use case ends.

**Extensions:**

* 1a. Missing mandatory fields (name, NRIC, sex, date of birth, phone, email, or address).
    * 1a1. DoctorWho shows an error message.

      Use case ends.

* 1b. Invalid field values.
    * 1b1. DoctorWho shows an error message.

      Use case ends.

* 1c. Added patient is a duplicate of an existing patient.
    * 1c1. DoctorWho shows an error message.

      Use case ends.

**Post conditions:**
* New patient appears at the bottom of the patient list.

**Use Case 02: Delete a Patient**

**Preconditions:**
* Staff has launched the DoctorWho application.
* Staff is at the command prompt.
* At least one patient exists in the list.

**Main Success Scenario:**
1. Staff requests to delete a specific patient using the index.
2. DoctorWho removes the patient from the system.
3. DoctorWho shows a success message.

   Use case ends.

**Extensions:**

* 1a. Invalid, missing or out of bounds index.
    * 1a1. DoctorWho shows an error message.

      Use case ends.

**Post conditions:**
* Patient is removed from the system.

**Use Case 03: List Patients**

**Preconditions:**
* Staff has launched the DoctorWho application.
* Staff is at the command prompt.

**Main Success Scenario:**
1. Staff requests to list all patients.
2. DoctorWho displays all patients in the list panel.
3. DoctorWho shows a success message.

   Use case ends.

**Post conditions:**
* All patients are displayed in the list panel.

**Use Case 04: Edit a Patient's Information**

**Preconditions:**
* Staff has launched the DoctorWho application.
* Staff is at the command prompt.
* At least one patient exists in the list.

**Main Success Scenario:**
1. Staff requests to edit a specific patient's information using the index.
2. DoctorWho updates the patient's information.
3. DoctorWho shows a success message with the updated details.

   Use case ends.

**Extensions:**

* 1a. Invalid, missing or out of bounds index.
    * 1a1. DoctorWho shows an error message.

      Use case ends.

* 1b. No fields provided to edit.
    * 1b1. DoctorWho shows an error message.

      Use case ends.

* 1c. Invalid field values.
    * 1c1. DoctorWho shows an error message.

      Use case ends.

* 1d. Edited details result in a duplicate patient.
    * 1d1. DoctorWho shows an error message.

      Use case ends.

* 1e. Staff provides allergies or conditions field with no value.
    * 1e1. DoctorWho clears all existing conditions or allergies respectively.

      Use case resumes from step 3.

**Post conditions:**
* Patient's information is updated in the system.

**Use Case 05: Schedule an appointment for an existing patient**

**Preconditions:**
* Staff has launched the DoctorWho application.
* Staff is at the command prompt.
* At least one patient exists in the list.

**Main Success Scenario:**
1. Staff requests to add an appointment for a specific patient using the index.
2. DoctorWho adds the appointment to the patient's record.
3. DoctorWho shows a success message with the appointment details.

   Use case ends.

**Extensions:**

* 1a. Invalid, missing or out of bounds index.
    * 1a1. DoctorWho shows an error message.

      Use case ends.

* 1b. Missing mandatory fields (datetime or duration).
    * 1b1. DoctorWho shows an error message.

      Use case ends.

* 1c. Invalid field values (e.g. invalid datetime format or duration out of range).
    * 1c1. DoctorWho shows an error message.

      Use case ends.

* 1d. Appointment is a duplicate of an existing appointment for the same patient.
    * 1d1. DoctorWho shows an error message.

      Use case ends.

* 1e. New appointment overlaps with an existing appointment.
    * 1e1. DoctorWho shows an error message.

      Use case ends.

**Post conditions:**
* Appointment is added and visible in the patient detail panel.

**Use Case 06: Delete Appointment**

**Preconditions:**
* Staff has launched the DoctorWho application.
* Staff is at the command prompt.
* At least one patient exists in the list.

**Main Success Scenario:**
1. Staff requests to delete the appointment of a specific patient using the index.
2. DoctorWho removes the appointment from the patient's record.
3. DoctorWho shows a success message.

   Use case ends.

**Extensions:**

* 1a. Invalid, missing or out of bounds index.
    * 1a1. DoctorWho shows an error message.

      Use case ends.

* 1b. Patient has no existing appointment.
    * 1b1. DoctorWho shows an error message.

      Use case ends.

**Post conditions:**
* Appointment is removed from the patient's record.

**Use Case 07: List Appointments**

**Preconditions:**
* Staff has launched the DoctorWho application.
* Staff is at the command prompt.

**Main Success Scenario:**

1. Staff requests to list appointments.
2. DoctorWho displays all appointments.
3. DoctorWho presents the appointments in ascending start date-time order.

   Use case ends.

**Extensions:**

* 1a. Staff requests to list appointments for a specific date.
    * 1a1. DoctorWho displays only appointments on the specified date.
    * 1a2. DoctorWho presents the results in ascending start date-time order.

      Use case ends.

* 1b. Staff enters an invalid date value.
    * 1b1. DoctorWho shows an error message.

      Use case ends.

* 1c. Staff enters an invalid date format.
    * 1c1. DoctorWho shows an error message.

      Use case ends.

* 2a. There are no appointments to display.
    * 2a1. DoctorWho shows an empty result list and a corresponding status message.

      Use case ends.

**Post conditions:**
* The currently displayed list is updated to show appointment-based results.
* If a date is provided, only appointments on that date are shown.

**Use Case 08: Find Patients**

**Preconditions:**
* Staff has launched the DoctorWho application.
* Staff is at the command prompt.

**Main Success Scenario:**
1. Staff requests to find patients by specifying a name keyword.
2. DoctorWho displays all patients whose names contain the input keyword.
3. DoctorWho shows a success message with the number of patients found.

   Use case ends.

**Extensions:**

* 1a. No patients found matching the keyword.
    * 1a1. DoctorWho shows a success message with 0 patients listed.

      Use case ends.

* 1b. Missing keyword.
    * 1b1. DoctorWho shows an error message.

      Use case ends.

**Post conditions:**
* Patient list panel displays only patients matching the input name.

**Use Case 09: Clear All Patients**

**Preconditions:**
* Staff has launched the DoctorWho application.
* Staff is at the command prompt.

**Main Success Scenario:**
1. Staff requests to clear all patient records.
2. DoctorWho removes all patients and their appointments from the system.
3. DoctorWho shows a success message.

   Use case ends.

**Post conditions:**
* Patient list is empty.

**Use Case 10: View Help**

**Preconditions:**
* Staff has launched the DoctorWho application.
* Staff is at the command prompt.

**Main Success Scenario:**
1. Staff requests to view help.
2. DoctorWho opens a help window with a link to the User Guide.

   Use case ends.

**Post conditions:**
* Help window is displayed to the staff.

**Use Case 11: Exit Application**

**Preconditions:**
* Staff has launched the DoctorWho application.

**Main Success Scenario:**
1. Staff requests to exit the application.
2. DoctorWho closes the application.

   Use case ends.

**Post conditions:**
* Application is closed.

### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2. Should be able to hold up to 1000 patient records without a noticeable sluggishness in performance for typical usage.
3. A user with a typing speed of at least 50 WPM should be able to complete any mandatory CRUD task (e.g., adding a
   patient) faster than an equivalent GUI.
4. Data must be saved locally in a human-readable JSON format to allow for manual inspection or external backup without
   using the app.
5. The system should handle corrupted data files by notifying the user and failing gracefully rather than crashing.
6. The system should be fully functional in an offline environment with no dependency on external servers or internet
   connectivity.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, macOS. (Relevant to *Setting up*)
* **GUI (Graphical User Interface)**: A visual interface that allows users to interact with the software through
  graphical elements like windows, buttons, and icons. (Relevant to *Architecture/UI*)
* **CLI (Command Line Interface)**: A text-based interface where the user provides input by typing commands. (Relevant
  to *Architecture/Logic*)
* **JavaFX**: The software platform and graphical library used to build the DoctorWho desktop interface. (Relevant to
  *UI Component*)
* **Prefix**: A short identifier followed by a forward slash (_e.g._ `n/` for name, `ic/` for NRIC) used to define arguments in a command.
* **Prefix-based Matching**: A parsing technique where data fields are identified by short leading characters (e.g.,
  `n/` for Name) rather than by their position in a sequence. (Relevant to *Logic Component*)
* **Medical Tag**: A general term encompassing both **Conditions** (_e.g._ Diabetes) and **Allergies** (_e.g._
  Penicillin). (Relevant to *Model Component*)
* **JSON**: JavaScript Object Notation, a text-based interchange data format, for storing or transmitting data. (
  Relevant to *Storage Component*)
* **CRUD**: An acronym for Create, Read, Update, and Delete—the four basic functions of persistent storage. (Relevant to
  *Implementation*)
* **MVP**: Minimum Viable Product; the core set of features required to make the app functional for Dr. Lee. (Relevant
  to *Appendix: Requirements*)
* **Private contact detail**: A contact detail that is not meant to be shared with others. (Relevant to *User Stories*)
* **Index**: A positive integer representing the position of an item in the currently displayed list in the UI. (
  Relevant to *Use Cases*)
* **Overlap**: A situation where a new appointment's time interval (start time + duration) intersects with an existing
  appointment's interval. (Relevant to *Use Cases*)
* **ISO 8601**: The international standard for the representation of dates and times (_e.g._ `YYYY-MM-DD`). (Relevant to
  *Use Cases/NFRs*)
* **NFR (Non-Functional Requirement)**: A requirement that specifies criteria that can be used to judge the operation of
  a system, rather than specific behaviors (_e.g._ security, reliability). (Relevant to *NFR Section*)
* **Scalability**: The measure of the system's ability to handle a growing amount of data (_e.g._ thousands of patients)
  without performance degradation. (Relevant to *NFR Section*)
* **Orphan Schedule**: An appointment record that remains in the system after the associated patient has been deleted.
  DoctorWho prevents this via automated purging. (Relevant to *NFR Section*)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch
    1. Download the jar file and copy into an empty folder
    2. Open a terminal, navigate to the folder and run `java -jar doctorwho.jar`

       Expected: Shows the GUI with a set of sample patients. The window size may not be optimum.

2. Saving window preferences
    1. Resize the window to an optimum size. Move the window to a different location. Close the window.
    2. Re-launch the app using `java -jar doctorwho.jar`

       Expected: The most recent window size and location is retained.

### Adding a patient

1. Adding a valid patient
    1. Test case: `add n/John Doe ic/T0123456H x/M dob/01-01-2000 p/98765432 e/johnd@example.com a/123 Clementi Ave`

       Expected: Patient added at the bottom of the list. Success message shown with patient name.

2. Adding a patient with missing mandatory fields
    1. Test case: `add n/John Doe p/98765432`

       Expected: No patient added. Error message shown with correct command format.

3. Adding a duplicate patient
    1. Prerequisites: Patient `John Doe` with NRIC `T0123456H` already exists (added in test case 1).
    2. Test case: `add n/John Doe ic/T0123456H x/M dob/01-01-2000 p/98765432 e/johnd@example.com a/123 Clementi Ave`

       Expected: No patient added. Error message indicating duplicate patient.

### Deleting a patient

1. Deleting a patient while all patients are being shown
    1. Prerequisites: List all patients using the `list` command. Multiple patients in the list.
    2. Test case: `delete 1`

       Expected: First patient is deleted from the list. Success message shown with patient name.

    3. Test case: `delete 0`

       Expected: No patient deleted. Error message shown.

    4. Other incorrect delete commands to try: `delete`, `delete x` (where x is larger than the list size)

       Expected: Similar to previous.

### Editing a patient

1. Editing a patient's phone number
    1. Prerequisites: At least one patient in the list.
    2. Test case: `edit 1 p/91234567`

       Expected: First patient's phone number updated. Success message shown.

2. Editing multiple fields of a patient (NRIC, phone, allergies, and medical condition)
    1. Prerequisites: At least one patient in the list.
    2. Test case: `edit 1 ic/T0234567J p/98557988 al/ mc/High BP`

   Expected: First patient's NRIC and phone are updated, existing allergies are cleared, and the medical condition "High BP" is added. Success message shown.

3. Editing a patient's date of birth
    1. Prerequisites: At least one patient in the list.
    2. Test case: `edit 1 dob/15-06-1995`

       Expected: First patient's date of birth updated. Success message shown.

4. Clearing all allergies
    1. Test case: `edit 1 al/`

       Expected: All allergies removed from first patient. Success message shown.

5. Editing with no fields provided
    1. Test case: `edit 1`

       Expected: No changes made. Error message shown.

### Adding an appointment

1. Adding a valid appointment
    1. Prerequisites: At least one patient in the list.
    2. Test case: `apt 1 d/01-04-2026 09:00 dur/60 note/Follow-up`

       Expected: Appointment added to first patient. Success message shown.

2. Adding an appointment with invalid date format
    1. Test case: `apt 1 d/2026-04-01 09:00 dur/60`

       Expected: No appointment added. Error message showing correct date format.

3. Adding an appointment with invalid duration
    1. Test case: `apt 1 d/01-04-2026 09:00 dur/0`

       Expected: No appointment added. Error message shown.

### Deleting an appointment

1. Deleting an existing appointment
    1. Prerequisites: Patient at index 1 has an existing appointment.
    2. Test case: `dapt 1`

       Expected: Appointment removed. Success message shown.

2. Deleting appointment from patient with no appointment
    1. Prerequisites: Patient at index 1 has no appointment.
    2. Test case: `dapt 1`

       Expected: No changes made. Error message shown.

### Listing appointments

1. Listing all appointments
    1. Test case: `lsapt`

       Expected: All appointments listed in ascending date-time order. Success message shows number of appointments.

2. Listing appointments by date
    1. Test case: `lsapt d/01-04-2026`

       Expected: Only appointments on 1 April 2026 shown.

3. Invalid date format
    1. Test case: `lsapt d/2026-04-01`

       Expected: Error message showing correct date format.

### Clearing all patients

1. Test case: `clear`

   Expected: All patients and appointments removed. Success message shown.

### Saving data

1. Dealing with missing data file
    1. Close the app.
    2. Navigate to `[JAR file location]/data/` and delete `doctorwho.json`.
    3. Re-launch the app.

       Expected: App starts with sample patient data. A new `doctorwho.json` is created.

2. Dealing with corrupted data file
    1. Close the app.
    2. Open `[JAR file location]/data/doctorwho.json` in a text editor.
    3. Delete a random line in the middle of the file and save.
    4. Re-launch the app.

       Expected: App starts with an empty patient list. Corrupted data file is discarded.
   
## **Appendix: Planned Enhancements**

### **Data Handling & Validation**
1. Include support for slashes (/) in patient name. Currently, we ask the user to remove slashes when entering the
   patient's name. However, this means that the stored patient name may not match their exact government name. We
   plan to implement apostrophe string enclosing to allow such special characters to be included in the name without
   conflicting with the special characters used for the argument prefixes.
2. Include cross-checks between a patient's date of birth and NRIC. Currently, we don't check that the patient's birth
   year matches their NRIC due to complexities and edge cases. Additionally, patients born before 1968 won't have their
   birth year as the first two digits of their NRIC, making this impossible in certain cases. We plan to implement a
   best-effort check that will flag possible mismatches.

### **Safety & Error Prevention**
3. Include confirmation for the `clear` command to protect the user from unintentionally clearing all their data. We
   plan to make it so that the user has to enter two consecutive clear commands before the data is actually cleared.

### **Patient Information Enhancements**
4. Include support for tagging patients as "High Risk". This feature allows doctors to quickly identify patients who 
   require extra caution. We plan to implement this as a visual indicator (e.g., tags or highlights) in the patient 
   profile to improve visibility and awareness during consultations.
5. Include support for marking allergies as "Severe". Currently, all allergies are treated equally.
   We plan to allow certain allergies to be flagged as severe so that they stand out more prominently in the patient profile.
6. Include a confirmation step before permanently deleting a patient record. This will help prevent accidental data 
   loss by requiring the user to confirm their intent before deletion is executed.
7. Include support for recording a patient's blood type. This feature would allow doctors to quickly access critical 
   information in emergency situations.

### **Search & Analysis Features**
8. Include functionality to list all patients taking a specific medication. This would be useful in scenarios such as 
   drug recalls or supply shortages, allowing doctors to quickly identify affected patients.

### **Advanced Medical Context**
9. Include support for linking related patients. This would allow doctors to identify family members and observe
   potential hereditary patterns in medical conditions.
10. Include support for attaching external specialist notes to a patient's record. This would provide a more 
    comprehensive view of the patient's medical history and ongoing care.

### **Advanced User Features**
11. Include support for chaining commands together. This would allow advanced users to execute multiple actions in a 
    single command (e.g., adding a patient and scheduling their first appointment), improving efficiency for advanced users.

## **Appendix: Effort**

**Difficulty level**: DoctorWho is significantly more complex than AB3. While AB3 manages a single entity type (Person), DoctorWho manages two entity types (Patient and Appointment) with relationships between them, requiring changes across all architectural layers.

**Challenges faced**:
- Implementing the `Appointment` entity required changes across Logic, Model, Storage and UI layers simultaneously
- Refactoring the generic `Tag` class into two specialised subclasses (`Allergy` and `Condition`) with separate validation rules, character limits and regex patterns required careful design to maintain extensibility
- Implementing overlap detection across all patients' appointments required non-trivial logic in the Model layer
- Updating the UI to include a dedicated `PatientDetailPanel` alongside the existing list panel required significant JavaFX work

**Effort**: Approximately equivalent to 1.5x the effort of AB3, given the addition of a second entity type and the tag hierarchy refactor.

**Achievements**:
- Successfully delivered all 7 MVP features on time
- Introduced a clean tag hierarchy (`Tag` → `Allergy`/`Condition`) that is easily extensible for future tag types
- Improved UI with a split-panel layout showing patient details alongside the patient list
