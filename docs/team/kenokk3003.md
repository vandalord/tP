---
layout: page
title: kenokk3003's Project Portfolio Page
---

### Project: DoctorWho

DoctorWho is a desktop application for managing patient records and appointments in a clinical setting. It targets clinic receptionists and administrative staff at single-doctor GP clinics, offering a fast Command Line Interface (CLI) with a JavaFX GUI. It is written in Java, and has about 10 kLoC.

As the team's Code Quality Lead, I was responsible for ensuring code quality standards were met throughout development, including implementation, review, and testing stages.

Given below are my contributions to the project.

* **New Feature**: Added NRIC support to the patient model, including checksum-based validation logic.
  (Pull request [\#113](https://github.com/AY2526S2-CS2103T-F10-1/tp/pull/113))

* **Enhancement**: Updated Add/Edit duplicate detection to check by NRIC rather than name.
  (Pull request [\#139](https://github.com/AY2526S2-CS2103T-F10-1/tp/pull/139))

* **Code contributed**: [RepoSense link](https://nus-cs2103-ay2526-s2.github.io/tp-dashboard/?search=KenOKK3003&breakdown=true)

* **Project management**:
    * Led large-scale terminology refactoring from `person` to `patient` across code and method-level references.
      (Pull requests [\#59](https://github.com/AY2526S2-CS2103T-F10-1/tp/pull/59),
      [\#84](https://github.com/AY2526S2-CS2103T-F10-1/tp/pull/84),
      [\#86](https://github.com/AY2526S2-CS2103T-F10-1/tp/pull/86))

* **Enhancements to existing features**:
    * Improved user-facing consistency by updating messages from "AddressBook" to "DoctorWho" / "patient book" terminology.
      (Pull request [\#274](https://github.com/AY2526S2-CS2103T-F10-1/tp/pull/274))
    * Fixed UI polish issues for patient details (e.g., NRIC icon and long-name wrapping behavior).
      (Pull requests [\#133](https://github.com/AY2526S2-CS2103T-F10-1/tp/pull/133),
      [\#135](https://github.com/AY2526S2-CS2103T-F10-1/tp/pull/135))

* **Documentation**:
  * User Guide:
    * Updated UG to include NRIC/Sex command details and corrected sample NRIC usage.
      (Pull requests [\#160](https://github.com/AY2526S2-CS2103T-F10-1/tp/pull/160),
      [\#278](https://github.com/AY2526S2-CS2103T-F10-1/tp/pull/278))
  * Developer Guide:
    * Updated UML and DG details to reflect NRIC, Sex, and DOB model updates, including tester NRIC references.
      (Pull requests [\#172](https://github.com/AY2526S2-CS2103T-F10-1/tp/pull/172),
      [\#173](https://github.com/AY2526S2-CS2103T-F10-1/tp/pull/173),
      [\#137](https://github.com/AY2526S2-CS2103T-F10-1/tp/pull/137))
