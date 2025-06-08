package laba5.client.l18n;

public class Messages_fr extends Messages {

    Object[][] contents = new Object[][]{
            {"registration.username", "Nom d'utilisateur"},
            {"registration.password", "Mot de passe"},
            {"registration.registerButton", "S'inscrire / Se connecter"},
            {"registration.emptyFields", "Les champs ne doivent pas être vides"},
            {"registration.connectionError", "Erreur de connexion au serveur"},

            {"menu.language", "Langue"},
            {"main.window.title", "Fenêtre principale"},
            {"main.user.label", "Utilisateur actif : "},
            {"main.tab.commands", "Commandes"},
            {"main.tab.dragons", "Dragons"},
            {"main.tab.visualization", "Visualisation"},
            {"main.tab.breeding", "Croiser des dragons"},
            {"main.window.title", "Fenêtre principale"},

            {"commands.execute.button", "Exécuter la commande"},
            {"commands.arguments.placeholder", "Arguments supplémentaires (si nécessaire)"},
            {"commands.info.client_side_executed", "Commande client exécutée."},
            {"commands.error.command_not_found", "Commande introuvable."},
            {"commands.error.communication_failed", "Échec de la communication avec le serveur."},

            {"dragon.table.header.name", "Nom"},
            {"dragon.table.header.age", "Âge"},
            {"dragon.table.header.coordinates", "Coordonnées"},
            {"dragon.table.header.creationDate", "Date de création"},
            {"dragon.table.header.type", "Type"},
            {"dragon.table.header.character", "Caractère"},
            {"dragon.table.header.killer", "Tueur"},
            {"dragon.table.header.killerLocation", "Lieu du tueur"},
            {"dragon.table.emptyValue", "—"},
            {"dragon.edit.title", "Modifier le dragon"},
            {"dragon.save.success", "Le dragon a été mis à jour avec succès."},
            {"dragon.save.error", "Erreur lors de la mise à jour du dragon : "},
            {"dragon.alert.title", "Message"},

            {"dragon.type.water", "Eau"},
            {"dragon.type.underground", "Souterrain"},
            {"dragon.type.fire", "Feu"},

            {"dragon.character.good", "Bon"},
            {"dragon.character.evil", "Méchant"},
            {"dragon.character.chaotic_evil", "Mal chaotique"},
            {"dragon.character.fickle", "Capricieux"},

            {"dragon.edit.header", "Modifiez les paramètres du dragon"},
            {"button.save", "Enregistrer"},
            {"dragon.field.name", "Nom :"},
            {"dragon.field.coordinate.x", "Coordonnée X :"},
            {"dragon.field.coordinate.y", "Coordonnée Y :"},
            {"dragon.field.age", "Âge :"},
            {"dragon.field.description", "Description :"},
            {"dragon.field.type", "Type :"},
            {"dragon.field.character", "Caractère :"},
            {"dragon.field.killer.has", "A un tueur"},
            {"dragon.field.killer.name", "Nom du tueur :"},
            {"dragon.field.killer.height", "Taille du tueur :"},
            {"dragon.field.killer.eyecolor", "Couleur des yeux :"},
            {"dragon.field.killer.haircolor", "Couleur des cheveux :"},
            {"dragon.field.killer.nationality", "Nationalité :"},
            {"dragon.field.killer.location.x", "Localisation X :"},
            {"dragon.field.killer.location.y", "Localisation Y :"},
            {"dragon.field.killer.location.z", "Localisation Z :"},
            {"error.invalid_input_format", "Format de données invalide !"},

            {"dragon.visualizer.button.reset", "Réinitialiser"},
            {"dragon.visualizer.error.fetch", "Erreur lors de la récupération des données sur les dragons."},
            {"dragon.visualizer.error.parsing", "Erreur lors du traitement des données sur les dragons."},

            {"alert.title.success", "Succès"},
            {"alert.title.info", "Information"},
            {"alert.title.error", "Erreur"},

            {"dragon.breeding.label.select", "Sélectionnez les dragons :"},
            {"dragon.breeding.parent1", "Parent 1"},
            {"dragon.breeding.parent2", "Parent 2"},
            {"dragon.breeding.button.breed", "Croiser les dragons"},
            {"dragon.breeding.alert.title.new_dragon", "Nouveau dragon"},
            {"dragon.breeding.alert.header.new_dragon", "Le dragon est né avec succès !"},
            {"dragon.breeding.error.select_parents", "Veuillez sélectionner les deux parents."},
            {"dragon.breeding.error.dragons_must_be_alive", "Les deux dragons doivent être vivants."},
            {"dragon.breeding.error.cannot_breed_self", "Impossible de croiser un dragon avec lui-même."},
            {"dragon.breeding.error.loading_dragons", "Erreur lors du chargement des dragons."},
            {"dragon.breeding.error.failed_adding", "Quelque chose s'est mal passé... Réessayez."},
            {"dragon.breeding.success.dragon_added", "Dragon ajouté avec succès."},
            {"dragon.unit.years", "ans"},

            {"error.empty_field", "Le champ ne peut pas être vide : "},
            {"error.does_not_meet_requirements", "ne satisfait pas les exigences."},
            {"error.dragon.age_must_be_greater_than_zero", "L'âge doit être supérieur à zéro."},
            {"error.dragon.character_required", "Le caractère est obligatoire."},
            {"error.killer_name_cannot_be_empty", "Le nom du tueur ne peut pas être vide."},
            {"error.killer_height_must_be_greater_than_zero", "La taille du tueur doit être supérieure à zéro."},

            {"error.enter_valid_numeric_values", "Veuillez entrer des valeurs numériques valides."},
            {"error.validation_failed", "Échec de la validation."},

            {"dragon.success.created", "Le dragon a été créé avec succès !"},
            {"optional", "facultatif"}
    };

    @Override
    protected Object[][] getContents() {
        return contents;
    }
}