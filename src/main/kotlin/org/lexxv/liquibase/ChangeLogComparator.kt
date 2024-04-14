package org.lexxv.liquibase


/**
 * Класс сравнения файлов миграции.
 *
 * Файлы миграции расположены в директориях именованные по версиям релиза. Liquibase сортирует по алфавиту полного пути,
 * данный клсаа меняет поведение компаратора на сравнение только по названию миграции
 *
 * @author Alexey Vorobyev <mister.alex49@yandex.ru>
 */
class ChangeLogComparator : Comparator<String> {
    override fun compare(o1: String, o2: String): Int {
        val file1 = o1.substringAfterLast("/")
        val file2 = o2.substringAfterLast("/")
        return file1.compareTo(file2)
    }
}
