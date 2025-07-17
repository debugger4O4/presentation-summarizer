### Суммаризатор презентаций.
Сервис занимается анализом файлов любого формата в которых содержится текст, анализирует и на выходе дает презентацию с кратким содержанием и графическими иллюстрациями.
<br />
Сервис интегрирован с https://developers.sber.ru/.
<br />
Чтобы пользоваться, необходимо завести аккунт, прочитать документацию и заполнить файл application.properties своими данными.
<br />
Для отправки сообщений необходимо [сертификат](https://www.gosuslugi.ru/crt). Выпущенный серт нужно упаковать в keystore.jks, который потом будет прикладываться к запросам.
Чтобы положить серты в .jks файл, надо октрыть терминал в месте расположения сертификатов и вполнить следующие команды:
1. Создаем хранилище ключей: ```keytool -genkeypair -alias <название> -keyalg RSA -keysize 2048 -keystore <название_хранилища>.jks -storepass <пароль>```;
2. Кладем скаченные/полученные сертификаты в .jks файл: ```keytool -importcert -alias <название> -file <название_сертификата>.crt -keystore <название_хранилища>.jks``` (Эта команда выполняется для каждого файла, который получен здесь [сертификат](https://www.gosuslugi.ru/crt));
3. Проверяем содержимое: ```keytool -list -v -keystore your_keystore.jks```.
<br />
Для запуска: {
<br />
&nbsp;&nbsp;&nbsp;&nbsp;
1.Клонировать репозиторий в свою среду разработки;
<br />
&nbsp;&nbsp;&nbsp;&nbsp;
2.Папка "back": {
<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
2.1.mvn install
<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
2.2.mvn spring-boot:run
<br />
&nbsp;&nbsp;&nbsp;&nbsp;
}
<br />
}
<br />
&nbsp;&nbsp;&nbsp;&nbsp;
3.Папка "front": {
<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
3.1.npm install
<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
3.2.npm run start
<br />
&nbsp;&nbsp;&nbsp;&nbsp;
}
<br />
}
<br />
#### Демонстрация работы:
https://github.com/user-attachments/assets/f25ddc3d-e4e4-45e8-a096-c01bfcc103db


