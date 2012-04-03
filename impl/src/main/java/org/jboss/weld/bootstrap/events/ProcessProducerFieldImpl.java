/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.weld.bootstrap.events;

import org.jboss.weld.bean.ProducerField;
import org.jboss.weld.manager.BeanManagerImpl;

import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.ProcessProducerField;
import java.lang.reflect.Type;

import static org.jboss.weld.util.reflection.Reflections.cast;

public class ProcessProducerFieldImpl<T, X> extends AbstractProcessProducerBean<T, X, ProducerField<T, X>> implements ProcessProducerField<T, X> {

    public static <T, X> void fire(BeanManagerImpl beanManager, ProducerField<T, X> bean) {
        if (beanManager.isBeanEnabled(bean)) {
            new ProcessProducerFieldImpl<T, X>(beanManager, bean) {
            }.fire();
        }
    }

    public ProcessProducerFieldImpl(BeanManagerImpl beanManager, ProducerField<T, X> bean) {
        super(beanManager, ProcessProducerField.class, new Type[]{bean.getEnhancedAnnotated().getBaseType(), bean.getEnhancedAnnotated().getDeclaringType().getBaseType()}, bean);
    }

    public AnnotatedField<T> getAnnotatedProducerField() {
        if (getBean().getEnhancedAnnotated() != null) {
            return cast(getBean().getEnhancedAnnotated());
        } else {
            return null;
        }
    }

    public AnnotatedParameter<T> getAnnotatedDisposedParameter() {
        // TODO
        throw new UnsupportedOperationException("Not implemented");
    }

}
