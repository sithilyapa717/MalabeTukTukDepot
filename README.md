# Malabe Tuk Tuk Depot

JavaFX inventory and point-of-sale application for a tuk-tuk parts depot. Manages inventory, dealers, shopping cart checkout with discounts, file persistence, and audit logging.

## Requirements

- JDK 21
- Maven (via IntelliJ bundled Maven or a local install)

## Run the application

In IntelliJ, open this folder as a Maven project:

1. **Lifecycle → clean**
2. **Plugins → javafx → javafx:run**

Data files are loaded from the `data/` folder on startup (`inventory.txt`, `dealers.txt`).

## Run tests

In IntelliJ: **Lifecycle → test**

Or from a terminal with Maven on PATH:

```bash
mvn test
```

39 JUnit 5 tests live in `src/test/java/`. Test fixtures are in `src/test/resources/`.

## Project layout

| Path | Purpose |
|------|---------|
| `src/main/java/` | Domain models, managers, parsers, JavaFX controllers |
| `src/main/resources/ui/` | FXML layouts (Scene Builder) |
| `src/test/java/` | JUnit test classes |
| `data/` | Live inventory, dealers, and audit log |
| `report/` | Coursework report (`Malabe_TukTuk_Depot_Report.docx`) |

## Tabs

- **Inventory** — CRUD, search, low-stock panel
- **Dealers** — pick four random dealers, select one for the cart
- **Cart** — dealer stock panel, add/remove items, bulk/synergy discounts, checkout
