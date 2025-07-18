# Описание проекта

__Цель проекта:__
В соответствии с ТЗ, для банка __Стар__ требуется разработать сервис, который должен формировать для клиентов банка 
рекомендации, в зависимости от имеющихся у клиента транзакций. Рекомендации формируются и отправляются клиенту по GET - 
запросу или через телеграм-бота.

База данных с транзакциями __transaction.mv.db__ 
предоставлена банком и используется только для чтения. В ней содержится 3 таблицы: _users, products и transactions_. 

## Первый этап работы
В соответствии с ТЗ на __первом__ этапе работы был разработан язык, позволяющий формализовать правила банка.
В языке есть 4 функции: __DEPOSIT__, __WITHDRAW__, __AMOUNT__, __DIFFERENCE__, которые соответствуют типу транзакции.
__DEPOSIT__ - операция пополнения;
__WITHDRAW__ - расходная операция;
__AMOUNT__ - сумма всех операций;
__DIFFERENCE__ - разница между доходными и расходными операциями.
Аргументами этих функций являются типы продуктов банка: __CREDIT__, __DEBIT__, __INVEST__ и __SAVING__. Аргументов 
может быть несколько. Они должны быть перечислены через запятую. 
Например, результатом __DEPOSIT(CREDIT)__ будет сумма всех транзакция с типом __DEPOSIT__, совершенных в отношении 
продукта __CREDIT__. Результат вычислений этой функции можно сравнить с любым числом с помощью операций сравнения:
__>__ - строго больше;
__<__ - строго меньше;
__=__ = равно;
__>=__ - больше или равно;
__<=__ - меньше или равно;
__<>__ - не равно;
Результатом операции сравнения всегда является значение __true__ или __false__.
Операции сравнения можно соединять логическими операторами __AND__ и __OR__, а также заключать их в скобки __()__. 

## Второй этап работы
Т.к. на втором этапе ТЗ было скорректировано, была разработана вторая реализация с учетом четырех динамических правил, 
описанных в ТЗ: __USER_OF__, __TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW__, __TRANSACTION_SUM_COMPARE__ и 
__ACTIVE_USER_OF__.

 Дополнительно к базе данных банка в рамках проекта были реализованы две базы база данных _recommendationsPart01.mv.db_ 
 и _recommendationsPart02.mv.db_, соответственно, для первой и второй частей проекта. В этих базах содержатся 
правила и рекомендации для клиентов, в случае, если правила выполняются. Структура баз данных в первой и второй частях 
 похожа.
Во второй части проекта в таблице __RECOMMENDATION__ используется два поля с id: __ID__ и __CONTRACT_ID__.
 Дело в том, что одна и та же рекомендация может быть предложена клиенту, если выполняется одна или другая группа 
 правил. Для каждой группы правил создается одна рекомендация с уникальным ID. __CONTRACT_ID__ - это ID, 
 предложенная банком. 

В соответствии с ТЗ реализован эндпоинт _GET /recommendation/{user_id}_. 
Данный get-запрос обрабатывается сервисом, который имплементирует интерфейс _RecommendationRuleSet_. Сервис по 
полученному id получает из базы данных банка __transaction.mv.db__ перечень всех 
транзакций клиента, сгруппированных по типам транзакций и типам продукта, в отношении которых была транзакция. На 
основании полученной информации проверяется выполнение правил банка и, в случае, если находятся рекомендации, для 
которых все правила выполняются, то эти рекомендации отправляются клиенту. В противном случае, возвращается пустой 
список.

В третьей части проекта к сервису была добавлена возможность кэшировать результаты запроса, собирать статистику
о результатах выполнения правил банка и получать информацию о package.

Для формирования диаграммы классов разработан сервисный класс __DiagramCreator__. Он в работе приложения участия не 
принимает. Данный класс, используя механизмы reflection, формирует список всех классов приложения в формате puml.