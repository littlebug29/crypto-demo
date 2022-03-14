# Crypto Demo
## Description
A demo app to show crypto currency list.
Data is fake, import locally.

## Requirements
- [x] `CurrencyListFragment` should receive an ArrayList of CurrencyInfo to create UI.
- [x] `DemoActivity` should provide 1 dataset. Currency List A of CurrencyInfo to `CurrencyListFragment`.
- [x]  The dataset should be queried from local db.
- [x] `DemoActivity` should provide 2 buttons to do the demo.
    - [x] First button to load the data and display.
    - [x] Second button for sorting currency list.

- [x] `CurrencyListFragment` should provide a hook of item click listener to the parent.
- [x] All the IO operations MUST NOT be in UI Thread.
- [x] Deal with concurrency issue when do sorting (fast double click of sorting button).

## Architecture
This project follows [the architecture guide](https://developer.android.com/jetpack/guide) from Google.
It includes 3 layers:
1. UI Layer: Activities, fragments, viewmodels, UI State models...
2. Domain Layer: Entities, UseCases
3. Data Layer: Repositories, Data Sources, Local Databases.

## What can improve more?
I didn't implement the paging for currency list, but in real app, we can add paging implementation.
