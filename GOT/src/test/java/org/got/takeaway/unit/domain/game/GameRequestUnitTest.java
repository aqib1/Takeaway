package org.got.takeaway.unit.domain.game;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import org.got.takeaway.domain.game.GameRequest;
import org.junit.jupiter.api.Test;

public class GameRequestUnitTest {

    @Test
    public void gameRequestTest() {
        PojoClass pojoClass = PojoClassFactory.getPojoClass(GameRequest.class);
        Validator validator = ValidatorBuilder.create().with(new GetterMustExistRule())
                .with(new GetterTester())
                .build();
        validator.validate(pojoClass);
    }
}
