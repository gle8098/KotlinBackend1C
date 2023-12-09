## Полезные команды для тестирования

### Пользователи

Создать пользователя
```bash
curl -F nickname="$USER_NICK" -F fullname="$USER_FULLNAME" -F password="$USER_PWD" "$URL/users/register"
```

Получить JWT токен для существующего пользователя
```bash
curl -F nickname="$USER_NICK" -F password="$USER_PWD" "$URL/users/login"
```

### Публикации

Создать публикацию. Возращается её UUID
```bash
curl -X PUT -H "Authorization: Bearer $USER_TOKEN" -d "$PUB_BODY" "$URL/publication/create"
```

Получить все данные о публикации
```bash
curl "$URL/publication/get/$PUB_ID"
```

Получить список всех публикаций (постранично)
```bash
curl "$URL/publication/getAllByPages/$PAGE_NO"
```

Отредактировать публикацию
```bash
curl -X PATCH -H "Authorization: Bearer $USER_TOKEN" -d "$PUB_BODY" "$URL/publication/edit/$PUB_ID"
```

Удалить публикацию
```bash
curl -X DELETE -H "Authorization: Bearer $USER_TOKEN" "$URL/publication/delete/$PUB_ID"
```