package laba5.client.l18n;

public class Messages_en extends Messages {

    Object[][] contents = new Object[][]{
            {"registration.username", "Username"},
            {"registration.password", "Password"},
            {"registration.registerButton", "Register / Login"},
            {"registration.emptyFields", "Fields cannot be empty"},
            {"registration.connectionError", "Connection error"},

            {"menu.language", "Language"},
            {"main.window.title", "Main Window"},
            {"main.user.label", "Active user: "},
            {"main.tab.commands", "Commands"},
            {"main.tab.dragons", "Dragons"},
            {"main.tab.visualization", "Visualization"},
            {"main.tab.breeding", "Breed Dragons"},
            {"main.window.title", "Main Window"},

            {"commands.execute.button", "Execute Command"},
            {"commands.arguments.placeholder", "Additional command arguments (if needed)"},
            {"commands.info.client_side_executed", "Client-side command executed."},
            {"commands.error.command_not_found", "Command not found."},
            {"commands.error.communication_failed", "Communication with server failed."},

            {"dragon.table.header.name", "Name"},
            {"dragon.table.header.age", "Age"},
            {"dragon.table.header.coordinates", "Coordinates"},
            {"dragon.table.header.creationDate", "Creation Date"},
            {"dragon.table.header.type", "Type"},
            {"dragon.table.header.character", "Character"},
            {"dragon.table.header.killer", "Killer"},
            {"dragon.table.header.killerLocation", "Killer Location"},
            {"dragon.table.emptyValue", "—"},
            {"dragon.edit.title", "Edit Dragon"},
            {"dragon.save.success", "Dragon successfully updated."},
            {"dragon.save.error", "Error updating dragon: "},
            {"dragon.alert.title", "Message"},

            {"dragon.type.water", "Water"},
            {"dragon.type.underground", "Underground"},
            {"dragon.type.fire", "Fire"},

            {"dragon.character.good", "Good"},
            {"dragon.character.evil", "Evil"},
            {"dragon.character.chaotic_evil", "Chaotic Evil"},
            {"dragon.character.fickle", "Fickle"},

            {"dragon.edit.header", "Modify dragon parameters"},
            {"button.save", "Save"},
            {"dragon.field.name", "Name:"},
            {"dragon.field.coordinate.x", "X Coordinate:"},
            {"dragon.field.coordinate.y", "Y Coordinate:"},
            {"dragon.field.age", "Age:"},
            {"dragon.field.description", "Description:"},
            {"dragon.field.type", "Type:"},
            {"dragon.field.character", "Character:"},
            {"dragon.field.killer.has", "Has Killer"},
            {"dragon.field.killer.name", "Killer Name:"},
            {"dragon.field.killer.height", "Killer Height:"},
            {"dragon.field.killer.eyecolor", "Eye Color:"},
            {"dragon.field.killer.haircolor", "Hair Color:"},
            {"dragon.field.killer.nationality", "Nationality:"},
            {"dragon.field.killer.location.x", "Location X:"},
            {"dragon.field.killer.location.y", "Location Y:"},
            {"dragon.field.killer.location.z", "Location Z:"},
            {"error.invalid_input_format", "Invalid input format!"},

            {"dragon.visualizer.button.reset", "Reset State"},
            {"dragon.visualizer.error.fetch", "Error fetching dragon data."},
            {"dragon.visualizer.error.parsing", "Error parsing dragon data."},

            {"alert.title.success", "Success"},
            {"alert.title.info", "Information"},
            {"alert.title.error", "Error"},

            {"dragon.breeding.label.select", "Select dragons:"},
            {"dragon.breeding.parent1", "Parent 1"},
            {"dragon.breeding.parent2", "Parent 2"},
            {"dragon.breeding.button.breed", "Breed Dragons"},
            {"dragon.breeding.alert.title.new_dragon", "New Dragon"},
            {"dragon.breeding.alert.header.new_dragon", "Dragon successfully born!"},
            {"dragon.breeding.error.select_parents", "Select both parents."},
            {"dragon.breeding.error.dragons_must_be_alive", "Both dragons must be alive."},
            {"dragon.breeding.error.cannot_breed_self", "Cannot breed a dragon with itself."},
            {"dragon.breeding.error.loading_dragons", "Failed to load dragons."},
            {"dragon.breeding.error.failed_adding", "Something went wrong... Please try again."},
            {"dragon.breeding.success.dragon_added", "Dragon successfully added."},
            {"dragon.unit.years", "years"},

            {"error.empty_field", "Field cannot be empty: "},
            {"error.does_not_meet_requirements", "does not meet requirements."},
            {"error.dragon.age_must_be_greater_than_zero", "Age must be greater than zero."},
            {"error.dragon.character_required", "Character is required."},
            {"error.killer_name_cannot_be_empty", "Killer name cannot be empty."},
            {"error.killer_height_must_be_greater_than_zero", "Killer height must be greater than zero."},

            {"error.enter_valid_numeric_values", "Enter valid numeric values."},
            {"error.validation_failed", "Validation failed."},

            {"dragon.success.created", "Dragon successfully created!"},
            {"optional", "optional"}
    };

    @Override
    protected Object[][] getContents() {
        return contents;
    }
}