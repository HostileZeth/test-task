INSERT INTO patient (first_name, last_name, patronymic, phone_number)
VALUES ('������', '�����', '�����������', '+79270064444'),
('����', '������', '����������', '+72283220123'),
('�������', '����', '�����', '+72283220123'),
('�����', '��������', '���', '+72283220123');

INSERT INTO doctor (first_name, last_name, patronymic, specialization)
VALUES ('�������', '����', '', '��������'),
('������', '�����', '', '��������'),
('����', '�������', '��������', '��������'),
('���', '��������', '�����', '��������');

INSERT INTO prescription (doctor_id, patient_id, description, date, priority, expiration_date)
VALUES (1, 2, '����������', '2020-01-01', 'NORMAL', '2021-01-01'),
(2, 1, '������', '2020-10-01', 'CITO', '2021-10-01'),
(2, 0, '�����', '2020-10-01', 'STATIM', '2021-10-01'),
(0, 2, '���-������ ���', '2020-10-01', 'CITO', '2021-10-01');