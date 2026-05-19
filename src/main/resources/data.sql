INSERT INTO users (user_id, user_name, user_password)
 VALUES (1, '鈴木一郎', 'himitu'), (2, '佐藤悠介', 'okusuri'), (3, '田中愛子', 'check');

 INSERT INTO medicine (medicine_id, medicine_name, medicine_note, medicine_count, medicine_check, medicine_check_morning, medicine_check_daytime, medicine_check_night, user_id)
  VALUES
  (1, '風邪薬', '食後1回2錠', 2, FALSE, FALSE, FALSE, FALSE, 1),
  (2, '頭痛薬', '食後1回1錠', 1, FALSE, FALSE, FALSE, FALSE, 1),
  (3, 'イブプロフェン', '食後1回1錠', 1, FALSE, FALSE, FALSE, FALSE, 2),
  (4, 'ビオフェルミン', '食後1回3錠', 3, FALSE, FALSE, FALSE, FALSE, 3),
  (5, 'ロキソニン', '食後1回1錠', 2, FALSE, FALSE, FALSE, FALSE, 1);