# ON EBX Reference Data

Reference-data management module for ON EBX FastTrack.

## Modules

- `referencedata-lib`: reusable Java services and components.
- `referencedata-web`: EBX module packaging, schemas, web resources, and reference datasets.

## Build

The build requires Java 17 or later and access to the configured EBX Maven repository.

```bash
mvn clean package
```

The project can also be included as the `referencedata` module in the ON EBX FastTrack Maven reactor.
