package com.harbin.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Yuanzz
 * @creat 2021-01-29-23:41
 */

public class ListValueConstraintValidator implements ConstraintValidator<ListValue,Integer> {

    Set<Integer> set = new HashSet<>();


    @Override
    public void initialize(ListValue constraintAnnotation) {
        int[] vals = constraintAnnotation.vals();
        for(int i : vals){
            set.add(i);
        }
    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return set.contains(integer);
    }
}