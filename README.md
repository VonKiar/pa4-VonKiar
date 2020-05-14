<p align = "center">
    <img width="500" height="auto" src="tracker/images/Logo.jpg" alt="Logo" />
</p>

**COVID19 GLOBAL STATUS TRACKING SYSTEM** is an application tracking COVID19 current statuses of total infected cases, total death cases and total recovered cases with up-to-date data tracking from online sources.

Actual PHP Censor versions and release branches:

| Mode            | Status                            | Description |
| :----------------- | :-------------------------------- | :------------------ |
| Standard           | Original Edition               | Displays total cases               |
| Differential       | DLC                            | Displays differentiate values each day|

[![first](tracker/images/firstShot.jpg)](tracker/images/firstShot.jpg)

More [screenshots](docs/en/screenshots.md).

* [Requirements](#requirements)
* [Features](#features)
* [Installing](#installing)
* [Configuration](#configuration)
* [References](#references)

## Requirements

* JAVA (Any version, Java 11.0.5 used to create this application);

* JAVAFX (Any version, Javafx 11.0.2 used to create this application);

* opencsv (Used to read CSV files, version 3.8 was used);

## Features

* Download csv files from online sources;

* Validate file's date to see if the download is necessary;

* Read csv files by sepate each cells via opencsv;

* Transform number format data into visual data using Line Chart;

* Customized theme included using stylesheet written in css;

* Simple design to prevents any confusion while using the application.

## Installing

* Change directory to the project's destination:

```bash
cd project-path
```

* Add javafx modules and opencsv to our applcation:

```bash
to be added.
```

## Configuration

There are several ways to set up the project:

* Add project without any project config (Runs "zero-config" plugins, including: Composer, TechnicalDebt, PHPLoc, 
PHPCpd, PHPCodeSniffer, PHPMessDetector, PHPDocblockChecker, PHPParallelLint, PHPUnit and Codeception);

* Similar to [Travis CI](https://travis-ci.org), to support PHP Censor in your project, you simply need to add a 
`.php-censor.yml` file to the root of your repository;

* Add project config in PHP Censor project page (And it will cancel file config from project repository);

The project config should look something like this:

```yml
setup:
  composer:
    action:    "install"
    directory: "."
test:
  php_unit:
    config: "phpunit.xml"
  php_mess_detector:
    allow_failures: true
  php_code_sniffer:
    standard: "PSR2"
  php_cpd:
    allow_failures: true
complete:
  email:
    default_mailto_address: admin@php-censor.local
```

More details about [configuring project](docs/en/configuring_project.md).

## References

The Humanitarian Data Exchange (2020, April 27). [Covid19 csv data](https://data.humdata.org/dataset/novel-coronavirus-2019-ncov-cases). Retrieved from https://data.humdata.org/dataset/novel-coronavirus-2019-ncov-cases
Oracle (2014, January). [Javafx chart example](https://docs.oracle.com/javafx/2/charts/line-chart.htm#CIHGBCFI). Retrieved from https://docs.oracle.com/javafx/2/charts/line-chart.htm#CIHGBCFI.