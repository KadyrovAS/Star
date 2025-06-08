DELETE FROM RECOMMENDATION;
DELETE FROM RECOMMENDATION_RULES;
DELETE FROM RULES;
INSERT INTO RECOMMENDATION_RULES (ID, PART, RULE_NAME) VALUES ('147f6a0f-3b91-413b-ab99-87f081d60d5a', 0, 'UseOneProductDebit');
INSERT INTO RECOMMENDATION_RULES (ID, PART, RULE_NAME) VALUES ('147f6a0f-3b91-413b-ab99-87f081d60d5a', 0, 'DoNotUseProductInvest');
INSERT INTO RECOMMENDATION_RULES (ID, PART, RULE_NAME) VALUES ('147f6a0f-3b91-413b-ab99-87f081d60d5a', 0, 'SumDepositProductSaving');
INSERT INTO RULES (NAME, PRODUCT_TYPE, TRANSACTION_TYPE, AMOUNT, CONDITION, ANNOTATION) VALUES ('UseOneProductDebit', 'DEBIT', '', 0, '>', 'Пользователь использует как минимум один продукт с типом DEBIT.')
INSERT INTO RULES (NAME, PRODUCT_TYPE, TRANSACTION_TYPE, AMOUNT, CONDITION, ANNOTATION) VALUES ('DoNotUseProductInvest', 'INVEST', '', 0, '=', 'Пользователь не использует продукты с типом INVEST.')
INSERT INTO RULES (NAME, PRODUCT_TYPE, TRANSACTION_TYPE, AMOUNT, CONDITION, ANNOTATION) VALUES ('SumDepositProductSaving', 'SAVING', 'DEPOSIT', 1000, '>', 'Сумма пополнений продуктов с типом SAVING больше 1000 ₽.')
INSERT INTO RECOMMENDATION (ID, NAME, ANNOTATION) VALUES ('147f6a0f-3b91-413b-ab99-87f081d60d5a', 'Invest 500', 'Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. Не упустите возможность разнообразить свой портфель, снизить риски и следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!')