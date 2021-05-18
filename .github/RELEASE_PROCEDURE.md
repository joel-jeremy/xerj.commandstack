# Release procedure

## Prerequisites
1. Merge feature branch to the `main` branch
2. Wait for `Build` and `Dev Deploy` workflows to complete successfully
3. Test that SNAPSHOT version is working as expected

## Release steps
1. Go to `Release` workflow
2. Select `main` branch
3. Input "`Release version`" e.g. 1.0.0
4. Input "`Development version after release`" e.g. 1.0.1  

    >Note: "-SNAPSHOT" will be automatically appended to the development version
5. Run `Release` workflow