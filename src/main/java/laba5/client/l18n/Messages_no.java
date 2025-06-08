package laba5.client.l18n;

public class Messages_no extends Messages {

    Object[][] contents = new Object[][]{
            {"registration.username", "Brukernavn"},
            {"registration.password", "Passord"},
            {"registration.registerButton", "Registrer / Logg inn"},
            {"registration.emptyFields", "Feltene kan ikke være tomme"},
            {"registration.connectionError", "Tilkoblingsfeil til serveren"},

            {"menu.language", "Språk"},
            {"main.window.title", "Hovedvindu"},
            {"main.user.label", "Aktiv bruker: "},
            {"main.tab.commands", "Kommandoer"},
            {"main.tab.dragons", "Drager"},
            {"main.tab.visualization", "Visualisering"},
            {"main.tab.breeding", "Avlsdrager"},
            {"main.window.title", "Hovedvindu"},

            {"commands.execute.button", "Utfør kommando"},
            {"commands.arguments.placeholder", "Ekstra kommandolinje-argumenter (hvis nødvendig)"},
            {"commands.info.client_side_executed", "Klientside-kommando utført."},
            {"commands.error.command_not_found", "Kommando ikke funnet."},
            {"commands.error.communication_failed", "Kommunikasjon med server feilet."},

            {"dragon.table.header.name", "Navn"},
            {"dragon.table.header.age", "Alder"},
            {"dragon.table.header.coordinates", "Koordinater"},
            {"dragon.table.header.creationDate", "Opprettelsesdato"},
            {"dragon.table.header.type", "Type"},
            {"dragon.table.header.character", "Karaktér"},
            {"dragon.table.header.killer", "Morder"},
            {"dragon.table.header.killerLocation", "Morderens plassering"},
            {"dragon.table.emptyValue", "—"},
            {"dragon.edit.title", "Rediger drage"},
            {"dragon.save.success", "Drage oppdatert."},
            {"dragon.save.error", "Feil ved oppdatering av drage: "},
            {"dragon.alert.title", "Melding"},

            {"dragon.type.water", "Vann"},
            {"dragon.type.underground", "Underjordisk"},
            {"dragon.type.fire", "Ild"},

            {"dragon.character.good", "God"},
            {"dragon.character.evil", "Ond"},
            {"dragon.character.chaotic_evil", "Kaotisk ond"},
            {"dragon.character.fickle", "Vekselaktig"},

            {"dragon.edit.header", "Endre dragedetaljer"},
            {"button.save", "Lagre"},
            {"dragon.field.name", "Navn:"},
            {"dragon.field.coordinate.x", "X-koordinat:"},
            {"dragon.field.coordinate.y", "Y-koordinat:"},
            {"dragon.field.age", "Alder:"},
            {"dragon.field.description", "Beskrivelse:"},
            {"dragon.field.type", "Type:"},
            {"dragon.field.character", "Karaktér:"},
            {"dragon.field.killer.has", "Har en morder"},
            {"dragon.field.killer.name", "Navn på morder:"},
            {"dragon.field.killer.height", "Høyde på morder:"},
            {"dragon.field.killer.eyecolor", "Øyefarge:"},
            {"dragon.field.killer.haircolor", "Hårfarge:"},
            {"dragon.field.killer.nationality", "Nasjonalitet:"},
            {"dragon.field.killer.location.x", "Sted X:"},
            {"dragon.field.killer.location.y", "Sted Y:"},
            {"dragon.field.killer.location.z", "Sted Z:"},
            {"error.invalid_input_format", "Ugyldig inndataformat!"},

            {"dragon.visualizer.button.reset", "Tilbakestill"},
            {"dragon.visualizer.error.fetch", "Feil ved henting av dragedata."},
            {"dragon.visualizer.error.parsing", "Feil ved behandling av dragedata."},

            {"alert.title.success", "Suksess"},
            {"alert.title.info", "Informasjon"},
            {"alert.title.error", "Feil"},

            {"dragon.breeding.label.select", "Velg drager:"},
            {"dragon.breeding.parent1", "Foreldre 1"},
            {"dragon.breeding.parent2", "Foreldre 2"},
            {"dragon.breeding.button.breed", "Fokk drager"},
            {"dragon.breeding.alert.title.new_dragon", "Ny drage"},
            {"dragon.breeding.alert.header.new_dragon", "Drage født suksessfullt!"},
            {"dragon.breeding.error.select_parents", "Velg begge foreldre."},
            {"dragon.breeding.error.dragons_must_be_alive", "Begge drager må være levende."},
            {"dragon.breeding.error.cannot_breed_self", "Kan ikke krysse drage med seg selv."},
            {"dragon.breeding.error.loading_dragons", "Feil ved lasting av drager."},
            {"dragon.breeding.error.failed_adding", "Noe gikk galt... Prøv igjen."},
            {"dragon.breeding.success.dragon_added", "Drage lagt til."},
            {"dragon.unit.years", "år"},

            {"error.empty_field", "Feltet kan ikke være tomt: "},
            {"error.does_not_meet_requirements", "tilfredsstiller ikke kravene."},
            {"error.dragon.age_must_be_greater_than_zero", "Alder må være større enn null."},
            {"error.dragon.character_required", "Karakter er påkrevd."},
            {"error.killer_name_cannot_be_empty", "Navn på morder kan ikke være tomt."},
            {"error.killer_height_must_be_greater_than_zero", "Morders høyde må være større enn null."},

            {"error.enter_valid_numeric_values", "Skriv inn gyldige numeriske verdier."},
            {"error.validation_failed", "Valideringsfeil."},

            {"dragon.success.created", "Drage ble opprettet!"},

            {"optional", "valgfritt"}
    };

    @Override
    protected Object[][] getContents() {
        return contents;
    }
}