DELETE FROM RECOMMENDATION;
DELETE FROM RECOMMENDATION_RULES;
DELETE FROM RULES;
INSERT INTO RECOMMENDATION_RULES (RECOMMENDATION_ID, RULE_ID) VALUES ('147f6a0f-3b91-413b-ab99-87f081d60d5a', '147f6a0f-3b91-413b-ab99-87f081d60d7c');
INSERT INTO RECOMMENDATION_RULES (RECOMMENDATION_ID, RULE_ID) VALUES ('147f6a0f-3b91-413b-ab99-87f081d60d5a', '147f6a0f-3b91-413b-ab99-87f081d60d8c');
INSERT INTO RECOMMENDATION_RULES (RECOMMENDATION_ID, RULE_ID) VALUES ('147f6a0f-3b91-413b-ab99-87f081d60d5a', '147f6a0f-3b91-413b-ab99-87f081d60d9c');
INSERT INTO RULES (ID, INSTRUCTION, ANNOTATION) VALUES ('147f6a0f-3b91-413b-ab99-87f081d60d7c', 'AMOUNT(DEBIT) > 0', 'Пользователь использует как минимум один продукт с типом DEBIT.')
INSERT INTO RULES (ID, INSTRUCTION, ANNOTATION) VALUES ('147f6a0f-3b91-413b-ab99-87f081d60d8c', 'AMOUNT(INVEST) = 0', 'Пользователь не использует продукты с типом INVEST.')
INSERT INTO RULES (ID, INSTRUCTION, ANNOTATION) VALUES ('147f6a0f-3b91-413b-ab99-87f081d60d9c', 'DEPOSIT(SAVING) > 1000', 'Сумма пополнений продуктов с типом SAVING больше 1000 ₽.')
INSERT INTO RECOMMENDATION (ID, NAME, ANNOTATION) VALUES ('147f6a0f-3b91-413b-ab99-87f081d60d5a', 'Invest 500', 'Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. Не упустите возможность разнообразить свой портфель, снизить риски и следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!')