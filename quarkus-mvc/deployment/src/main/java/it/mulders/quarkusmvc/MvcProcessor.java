/*
 * Copyright 2019 Maarten Mulders and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.mulders.quarkusmvc;

import javax.inject.Inject;
import javax.mvc.RedirectScoped;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.ContextRegistrarBuildItem;
import io.quarkus.arc.processor.ContextRegistrar;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CapabilityBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageResourceBuildItem;
import it.mulders.quarkusmvc.runtime.RedirectScopeContext;
import org.eclipse.krazo.MvcContextImpl;
import org.eclipse.krazo.KrazoConfig;
import org.eclipse.krazo.binding.BeanValidationProducer;
import org.eclipse.krazo.binding.BindingResultManager;
import org.eclipse.krazo.binding.ConstraintViolationTranslator;
import org.eclipse.krazo.binding.convert.ConverterRegistry;
import org.eclipse.krazo.binding.convert.MvcConverterProvider;
import org.eclipse.krazo.binding.validate.ValidationInterceptor;
import org.eclipse.krazo.cdi.AroundControllerInterceptor;
import org.eclipse.krazo.cdi.RedirectScopeManager;
import org.eclipse.krazo.core.Messages;
import org.eclipse.krazo.core.ModelsImpl;
import org.eclipse.krazo.core.ViewableWriter;
import org.eclipse.krazo.core.ViewResponseFilter;
import org.eclipse.krazo.engine.FaceletsViewEngine;
import org.eclipse.krazo.engine.JspViewEngine;
import org.eclipse.krazo.engine.ViewEngineFinder;
import org.eclipse.krazo.event.AfterControllerEventImpl;
import org.eclipse.krazo.event.BeforeControllerEventImpl;
import org.eclipse.krazo.event.AfterProcessViewEventImpl;
import org.eclipse.krazo.event.BeforeProcessViewEventImpl;
import org.eclipse.krazo.event.ControllerRedirectEventImpl;
import org.eclipse.krazo.jaxrs.JaxRsContextProducer;
import org.eclipse.krazo.lifecycle.EventDispatcher;
import org.eclipse.krazo.lifecycle.RequestLifecycle;
import org.eclipse.krazo.locale.DefaultLocaleResolver;
import org.eclipse.krazo.locale.LocaleResolverChain;
import org.eclipse.krazo.security.CsrfImpl;
import org.eclipse.krazo.security.CsrfProtectFilter;
import org.eclipse.krazo.security.CsrfValidateFilter;
import org.eclipse.krazo.security.CsrfTokenManager;
import org.eclipse.krazo.security.EncodersImpl;
import org.eclipse.krazo.uri.ApplicationUris;
import org.eclipse.krazo.uri.UriTemplateParser;
import org.eclipse.krazo.util.CdiUtils;

public class MvcProcessor {
    @Inject
    BuildProducer<NativeImageResourceBuildItem> resource;

    @BuildStep
    public FeatureBuildItem featureBuildItem() {
        return new FeatureBuildItem("quarkus-mvc");
    }

    @BuildStep
    public CapabilityBuildItem capability() {
        return new CapabilityBuildItem("it.mulders.quarkus-mvc");
    }

    @BuildStep
    ContextRegistrarBuildItem registerRedirectScope() {
        return new ContextRegistrarBuildItem(new ContextRegistrar() {
            public void register(RegistrationContext registrationContext) {
                registrationContext.configure(RedirectScoped.class).normal().contextClass(RedirectScopeContext.class).done();
            }
        }, RedirectScoped.class);
    }

    @BuildStep
    void registerNativeImageResources() {
        resource.produce(new NativeImageResourceBuildItem(
                "/META-INF/context.xml"
        ));
    }

    @BuildStep
    public void krazoBeans(BuildProducer<AdditionalBeanBuildItem> additionalBeans) {
        final AdditionalBeanBuildItem buildItem = AdditionalBeanBuildItem.builder().setUnremovable().addBeanClasses(
                // Copied over from org.eclipse.krazo.cdi.KrazoCdiExtension

                // .
                MvcContextImpl.class,
                KrazoConfig.class,

                // binding
                BeanValidationProducer.class,
                BindingResultManager.class,
                ConstraintViolationTranslator.class,
                ConverterRegistry.class,
                MvcConverterProvider.class,

                // core
                Messages.class,
                ModelsImpl.class,
                ViewableWriter.class,
                ViewResponseFilter.class,

                // lifecycle
                EventDispatcher.class,
                RequestLifecycle.class,

                // engine
                FaceletsViewEngine.class,
                JspViewEngine.class,
                ViewEngineFinder.class,

                // security
                CsrfImpl.class,
                CsrfProtectFilter.class,
                CsrfValidateFilter.class,
                CsrfTokenManager.class,
                EncodersImpl.class,

                // util
                CdiUtils.class,

                // cdi
                RedirectScopeManager.class,
                ValidationInterceptor.class,
                AroundControllerInterceptor.class,

                // event
                AfterControllerEventImpl.class,
                AfterProcessViewEventImpl.class,
                BeforeControllerEventImpl.class,
                BeforeProcessViewEventImpl.class,
                ControllerRedirectEventImpl.class,

                // locale
                LocaleResolverChain.class,
                DefaultLocaleResolver.class,

                // jaxrs
                JaxRsContextProducer.class,

                // uri
                ApplicationUris.class,
                UriTemplateParser.class
        ).build();
        additionalBeans.produce(buildItem);
    }

}
