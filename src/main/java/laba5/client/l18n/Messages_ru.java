package laba5.client.l18n;

public class Messages_ru extends Messages{

    Object[][] contents = new Object[][]{
            {"registration.username", "Имя пользователя"},
            {"registration.password", "Пароль"},
            {"registration.registerButton", "Зарегистрироваться / войти в аккаунт"},
            {"registration.emptyFields", "Поля не должны быть пустыми"},
            {"registration.connectionError", "Ошибка подключения к серверу"},

            {"menu.language", "Язык"},
            {"main.window.title", "Основное окно"},
            {"main.user.label", "Активный пользователь: "},
            {"main.tab.commands", "Команды"},
            {"main.tab.dragons", "Дракончики"},
            {"main.tab.visualization", "Визуализация"},
            {"main.tab.breeding", "Скрестить драконов"},
            {"main.window.title", "Основное окно"},

            {"commands.execute.button", "Выполнить команду"},
            {"commands.arguments.placeholder", "Дополнительные аргументы команды (если необходимо)"},
            {"commands.info.client_side_executed", "Клиентская команда выполнена."},
            {"commands.error.command_not_found", "Команда не найдена."},
            {"commands.error.communication_failed", "Ошибка связи с сервером."},

            {"dragon.table.header.name", "Имя"},
            {"dragon.table.header.age", "Возраст"},
            {"dragon.table.header.coordinates", "Координаты"},
            {"dragon.table.header.creationDate", "Дата создания"},
            {"dragon.table.header.type", "Тип"},
            {"dragon.table.header.character", "Характер"},
            {"dragon.table.header.killer", "Убийца"},
            {"dragon.table.header.killerLocation", "Местоположение убийцы"},
            {"dragon.table.emptyValue", "—"},
            {"dragon.edit.title", "Редактировать дракона"},
            {"dragon.save.success", "Дракон успешно обновлён."},
            {"dragon.save.error", "Ошибка при обновлении дракона: "},
            {"dragon.alert.title", "Сообщение"},

            {"dragon.type.water", "Водный"},
            {"dragon.type.underground", "Подземный"},
            {"dragon.type.fire", "Огненный"},

            {"dragon.character.good", "Добрый"},
            {"dragon.character.evil", "Злой"},
            {"dragon.character.chaotic_evil", "Хаотичное зло"},
            {"dragon.character.fickle", "Непостоянный"},

            {"dragon.edit.title", "Редактировать дракона"},
            {"dragon.edit.header", "Измените параметры дракона"},
            {"button.save", "Сохранить"},
            {"dragon.field.name", "Имя:"},
            {"dragon.field.coordinate.x", "Координата X:"},
            {"dragon.field.coordinate.y", "Координата Y:"},
            {"dragon.field.age", "Возраст:"},
            {"dragon.field.description", "Описание:"},
            {"dragon.field.type", "Тип:"},
            {"dragon.field.character", "Характер:"},
            {"dragon.field.killer.has", "Есть убийца"},
            {"dragon.field.killer.name", "Имя убийцы:"},
            {"dragon.field.killer.height", "Рост убийцы:"},
            {"dragon.field.killer.eyecolor", "Цвет глаз:"},
            {"dragon.field.killer.haircolor", "Цвет волос:"},
            {"dragon.field.killer.nationality", "Национальность:"},
            {"dragon.field.killer.location.x", "Местоположение X:"},
            {"dragon.field.killer.location.y", "Местоположение Y:"},
            {"dragon.field.killer.location.z", "Местоположение Z:"},
            {"error.invalid_input_format", "Некорректный формат данных!"},

            {"dragon.visualizer.button.reset", "Сбросить состояние"},
            {"dragon.visualizer.error.fetch", "Ошибка при получении данных о драконах."},
            {"dragon.visualizer.error.parsing", "Ошибка при обработке данных о драконах."},

            {"alert.title.success", "Успех"},
            {"alert.title.info", "Информация"},
            {"alert.title.error", "Ошибка"},

            {"dragon.breeding.label.select", "Выберите драконов:"},
            {"dragon.breeding.parent1", "Родитель 1"},
            {"dragon.breeding.parent2", "Родитель 2"},
            {"dragon.breeding.button.breed", "Скрестить драконов"},
            {"dragon.breeding.alert.title.new_dragon", "Новый дракон"},
            {"dragon.breeding.alert.header.new_dragon", "Дракон успешно рождён!"},
            {"dragon.breeding.error.select_parents", "Выберите обоих родителей."},
            {"dragon.breeding.error.dragons_must_be_alive", "Оба дракона должны быть живыми."},
            {"dragon.breeding.error.cannot_breed_self", "Нельзя скрещивать дракона с самим собой."},
            {"dragon.breeding.error.loading_dragons", "Ошибка загрузки драконов."},
            {"dragon.breeding.error.failed_adding", "Что-то пошло не так... Попробуйте ещё раз."},
            {"dragon.breeding.success.dragon_added", "Дракон успешно добавлен."},
            {"dragon.unit.years", "лет"},

            {"dragon.edit.title", "Введите данные дракона"},
            {"dragon.field.name", "Имя"},
            {"dragon.field.coordinate.x", "Координата X"},
            {"dragon.field.coordinate.y", "Координата Y"},
            {"dragon.field.age", "Возраст"},
            {"dragon.field.description", "Описание"},
            {"dragon.field.type", "Тип"},
            {"dragon.field.character", "Характер"},
            {"dragon.field.killer.has", "Есть убийца"},
            {"dragon.field.killer.name", "Имя убийцы"},
            {"dragon.field.killer.height", "Рост убийцы"},
            {"dragon.field.killer.eyecolor", "Цвет глаз"},
            {"dragon.field.killer.haircolor", "Цвет волос"},
            {"dragon.field.killer.nationality", "Национальность"},
            {"dragon.field.killer.location.x", "Местоположение X"},
            {"dragon.field.killer.location.y", "Местоположение Y"},
            {"dragon.field.killer.location.z", "Местоположение Z"},

            {"error.empty_field", "Поле не может быть пустым: "},
            {"error.does_not_meet_requirements", "не соответствует требованиям."},
            {"error.dragon.age_must_be_greater_than_zero", "Возраст должен быть больше нуля."},
            {"error.dragon.character_required", "Характер обязателен."},
            {"error.killer_name_cannot_be_empty", "Имя убийцы не может быть пустым."},
            {"error.killer_height_must_be_greater_than_zero", "Рост убийцы должен быть больше нуля."},

            {"error.enter_valid_numeric_values", "Введите корректные числовые значения."},
            {"error.validation_failed", "Ошибка валидации данных."},

            {"dragon.success.created", "Дракон успешно создан!"},
            {"alert.title.success", "Успех"},
            {"alert.title.error", "Ошибка"},
            {"optional", "необязательно"}
    };

    @Override
    protected Object[][] getContents() {
        return contents;
    }
}
