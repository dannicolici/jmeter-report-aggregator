# jmeter-report-aggregator

Tool for generating visual aggregate reports from individual jmeter test results.
It assumes the test results are XML files containing httpSample tags.

It will generate 3 PNG files: min.png, avg.png & max.png, that represent the aggregated
values, as described by the filenames (i.e. aggregated min/avg/max times, from individual test runs).

## Installation

    Download sources from https://github.com/dannicolici/jmeter-report-aggregator
    Run `lein uberjar`

## Usage

    $ java -jar jmeter-report-aggregator-0.1.0-standalone.jar [args]

## Options

    No options supported.

## Examples

    java -jar jmeter-report-aggregator-0.1.0-standalone.jar 1.xml 2.xml 3.xml



Copyright © 2019
