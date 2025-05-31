package laba5.client.l18n;

public class Messages_en_ZA extends Messages{

    Object[][] contents = new Object[][]{
            {"registration.title", "Регистрация"},
            {"registration.username", "Имя пользователя"},
            {"registration.password", "Пароль"},
            {"registration.registerButton", "Зарегистрироваться/ войти в аккаунт"},
            {"registration.emptyFields", "******"}
    };

    @Override
    protected Object[][] getContents() {
        return contents;
    }
}
