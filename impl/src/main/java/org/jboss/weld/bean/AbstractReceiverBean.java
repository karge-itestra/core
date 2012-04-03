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
package org.jboss.weld.bean;

import static org.jboss.weld.logging.Category.BEAN;
import static org.jboss.weld.logging.LoggerFactory.loggerFactory;
import static org.jboss.weld.logging.messages.BeanMessage.CIRCULAR_CALL;

import java.lang.reflect.Member;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.BeanAttributes;

import org.jboss.weld.annotated.enhanced.EnhancedAnnotatedMember;
import org.jboss.weld.bootstrap.BeanDeployerEnvironment;
import org.jboss.weld.bootstrap.api.ServiceRegistry;
import org.jboss.weld.context.WeldCreationalContext;
import org.jboss.weld.manager.BeanManagerImpl;
import org.slf4j.cal10n.LocLogger;

/**
 * @author pmuir
 * @author alesj
 */
public abstract class AbstractReceiverBean<X, T, S extends Member> extends AbstractBean<T, S> {

    private static final LocLogger log = loggerFactory().getLogger(BEAN);

    private final AbstractClassBean<X> declaringBean;

    public AbstractReceiverBean(BeanAttributes<T> attributes, String idSuffix, AbstractClassBean<X> declaringBean, BeanManagerImpl beanManager, ServiceRegistry services) {
        super(attributes, idSuffix, beanManager, services);
        this.declaringBean = declaringBean;
    }

    /**
     * Gets the receiver of the product
     *
     * @param creationalContext the creational context
     * @return The receiver
     */
    protected Object getReceiver(CreationalContext<?> creationalContext) {
        // This is a bit dangerous, as it means that producer methods can end up
        // executing on partially constructed instances. Also, it's not required
        // by the spec...
        if (getEnhancedAnnotated().isStatic()) {
            return null;
        } else {
            if (creationalContext instanceof WeldCreationalContext<?>) {
                WeldCreationalContext<?> creationalContextImpl = (WeldCreationalContext<?>) creationalContext;
                final X incompleteInstance = creationalContextImpl.getIncompleteInstance(getDeclaringBean());
                if (incompleteInstance != null) {
                    log.warn(CIRCULAR_CALL, getEnhancedAnnotated(), getDeclaringBean());
                    return incompleteInstance;
                }
            }
            return beanManager.getReference(getDeclaringBean(), creationalContext, true);
        }
    }


    /**
     * Returns the declaring bean
     *
     * @return The bean representation
     */
    public AbstractClassBean<X> getDeclaringBean() {
        return declaringBean;
    }

    @Override
    public abstract EnhancedAnnotatedMember<T, ?, S> getEnhancedAnnotated();

}
