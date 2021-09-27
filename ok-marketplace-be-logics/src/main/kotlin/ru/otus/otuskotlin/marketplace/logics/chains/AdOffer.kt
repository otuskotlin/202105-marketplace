package ru.otus.otuskotlin.marketplace.logics.chains

import ru.otus.otuskotlin.marketplace.backend.common.context.MpContext
import ru.otus.otuskotlin.marketplace.backend.repo.common.DbAdFilterRequest
import ru.otus.otuskotlin.marketplace.backend.repo.common.DbAdModelRequest
import ru.otus.otuskotlin.marketplace.common.cor.ICorExec
import ru.otus.otuskotlin.marketplace.common.cor.chain
import ru.otus.otuskotlin.marketplace.common.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.logics.chains.helpers.mpValidation
import ru.otus.otuskotlin.marketplace.logics.chains.stubs.adOfferStub
import ru.otus.otuskotlin.marketplace.logics.workers.*
import ru.otus.otuskotlin.marketplace.logics.workers.answerPrepareChain
import ru.otus.otuskotlin.marketplace.logics.workers.chainInitWorker
import ru.otus.otuskotlin.marketplace.logics.workers.checkOperationWorker
import ru.otus.otuskotlin.marketplace.logics.workers.chooseDb
import ru.otus.otuskotlin.marketplace.validation.validators.ValidatorStringNonEmpty

object AdOffer: ICorExec<MpContext> by chain<MpContext>({
    checkOperationWorker(
        title = "Проверка операции",
        targetOperation = MpContext.MpOperations.OFFER,
    )
    chainInitWorker(title = "Инициализация чейна")
    chooseDb(title = "Выбираем БД или STUB")
    adOfferStub(title = "Обработка стабкейса для OFFER")

    mpValidation {
        validate<String?> {
            on {
                requestAdId.asString() }
            validator(ValidatorStringNonEmpty(field = "id"))
        }
    }

    repoOffer(title = "Поиск предложений для объекта в БД")

    answerPrepareChain(title = "Подготовка ответа")
}).build()
