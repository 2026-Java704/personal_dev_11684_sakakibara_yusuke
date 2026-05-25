INSERT INTO users (name, password)
 VALUES ('鈴木一郎', 'himitu'), ('佐藤悠介', 'okusuri'), ('田中愛子', 'check');

 INSERT INTO medicine ( name, note, count,m_check, morning, daytime, night, users_id)
  VALUES
  ( '風邪薬', '食後1回2錠', 2, FALSE,TRUE, FALSE, FALSE, 1),
  ( '頭痛薬', '食後1回1錠', 1, FALSE,FALSE, TRUE, FALSE, 1),
  ( 'イブプロフェン', '食後1回1錠', 1, FALSE,FALSE, FALSE, TRUE, 2),
  ( 'ビオフェルミン', '食後1回3錠', 3, FALSE,FALSE, TRUE, FALSE,3),
  ( 'ロキソニン', '食後1回1錠', 2, FALSE,TRUE, FALSE, FALSE, 1);