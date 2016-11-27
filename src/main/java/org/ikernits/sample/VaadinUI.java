package org.ikernits.sample;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.BaseEncoding;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.ikernits.sample.VaadinForm.FormProperty;
import org.ikernits.sample.vaadin.components.HighChart;
import org.ikernits.vaadin.VaadinBuilders;
import org.ikernits.vaadin.VaadinComponentStyles.ColorStyle;

import org.joda.time.DateTime;

import javax.crypto.Cipher;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import static org.ikernits.vaadin.VaadinComponentAttributes.ComponentAttributes.vaHeight100;
import static org.ikernits.vaadin.VaadinComponentAttributes.ComponentAttributes.vaReadOnly;
import static org.ikernits.vaadin.VaadinComponentAttributes.ComponentAttributes.vaSizeFull;
import static org.ikernits.vaadin.VaadinComponentAttributes.ComponentAttributes.vaStyleMarginTiny;
import static org.ikernits.vaadin.VaadinComponentAttributes.ComponentAttributes.vaStyleMonospace;
import static org.ikernits.vaadin.VaadinComponentAttributes.ComponentAttributes.vaStylePaddingTiny;
import static org.ikernits.vaadin.VaadinComponentAttributes.ComponentAttributes.vaStyleTiny;
import static org.ikernits.vaadin.VaadinComponentAttributes.ComponentAttributes.vaWidth100;
import static org.ikernits.vaadin.VaadinComponentAttributes.LayoutAttributes.vaMargin;
import static org.ikernits.vaadin.VaadinComponentAttributes.LayoutAttributes.vaSpacing;

/**
 * Created by ikernits on 10/10/15.
 */
@Title("Vaadin UI")
@Theme("valo-ext")
@Push(value = PushMode.DISABLED)
public class VaadinUI extends UI {


    static Logger log = Logger.getLogger(VaadinUI.class);
    Property<Integer> count = new ObjectProperty<>(0);
    ObjectProperty<String> theme = new ObjectProperty<>("valo");
    Property<String> text = new ObjectProperty<>("123");


    protected Layout createVaTestLayout() {
        VerticalLayout vaTestLayout = new VerticalLayout();

        VerticalLayout layout1 = new VerticalLayout();

        layout1.addComponent(new Label("Hello vaadin"));
        layout1.addComponent(new Button("Increment") {{
            addClickListener(clickEvent -> {
                count.setValue(count.getValue() + 1);
            });
            setStyleName(ValoTheme.BUTTON_TINY);
        }});
        layout1.setMargin(true);
        layout1.setSpacing(true);
        layout1.setSizeFull();

        TextField tf = new TextField(count);
        layout1.addComponent(tf);

        Panel panel1 = new Panel("Test panel 1", layout1);
        vaTestLayout.addComponent(panel1);

        VerticalLayout layout2 = new VerticalLayout();
        layout2.setMargin(true);
        layout2.setSpacing(true);

        Label l2 = new Label("123");
        l2.setImmediate(true);
        l2.setValue("321");

        Label l22 = new Label(text);
        l22.setImmediate(true);

        TextField tf2 = new TextField(text);
        tf2.setImmediate(true);
        tf2.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);
        tf2.addTextChangeListener(event -> {
            log.info("text updated: '" + event.getText());
            l2.setValue(event.getText());
        });

        layout2.addComponents(l2, l22, tf2);
        Panel p2 = new Panel("Test panel 2", layout2);
        vaTestLayout.addComponent(p2);
        vaTestLayout.setMargin(true);

        ComboBox themeCb = new ComboBox("Theme", ImmutableList.of(
                "valo",
                "reindeer",
                "chameleon",
                "runo",
                "liferay",
                "valo-ext-blueprint",
                "valo-ext-dark",
                "valo-ext-facebook",
                "valo-ext-flat",
                "valo-ext-flatdark",
                "valo-ext-light",
                "valo-ext-metro"));

        themeCb.setNullSelectionAllowed(false);
        themeCb.setTextInputAllowed(false);
        themeCb.setPropertyDataSource(theme);
        themeCb.setPageLength(0);

        theme.addValueChangeListener(event -> {
            setTheme(theme.getValue());
        });

        vaTestLayout.addComponent(themeCb);
        vaTestLayout.setSpacing(true);
        return vaTestLayout;
    }

    protected Component createCryptoLayout() {
        VerticalLayout keysLayout = VaadinBuilders.verticalLayout()
                .setAttributes(vaMargin, vaSpacing, vaWidth100)
                .build();

        Property<String> privatePem = new ObjectProperty<>("");
        keysLayout.addComponent(VaadinBuilders.textArea()
                        .setAttributes(vaWidth100, vaStyleTiny, vaStyleMonospace)
                        .setCaption("Private PEM key")
                        .setPropertyDataSource(privatePem)
                        .setHeight(32, Unit.EM)
                        .build()
        );

        Property<String> publicPem = new ObjectProperty<>("");
        keysLayout.addComponent(VaadinBuilders.textArea()
                        .setAttributes(vaWidth100, vaStyleTiny, vaStyleMonospace)
                        .setCaption("Public PEM key")
                        .setPropertyDataSource(publicPem)
                        .setHeight(11, Unit.EM)
                        .build()
        );

        VerticalLayout encryptLayout = VaadinBuilders.verticalLayout()
                .setAttributes(vaMargin, vaSpacing, vaWidth100)
                .build();

        Property<String> textToEncrypt = new ObjectProperty<>("");
        Property<String> encryptedText = new ObjectProperty<>("");
        Property<String> decryptedText = new ObjectProperty<>("");

        encryptLayout.addComponent(VaadinBuilders.textField()
                        .setAttributes(vaWidth100)
                        .setCaption("Plain Text")
                        .setPropertyDataSource(textToEncrypt)
                        .build()
        );

        encryptLayout.addComponent(VaadinBuilders.button()
                        .setCaption("Encrypt")
                        .addClickListener(e -> {
                            try {
                                Security.addProvider(new BouncyCastleProvider());
                                PEMReader pemReader = new PEMReader(new StringReader(privatePem.getValue()));
                                KeyPair keyPair = (KeyPair) pemReader.readObject();
                                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                                cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());
                                cipher.update(textToEncrypt.getValue().getBytes());
                                encryptedText.setValue(BaseEncoding.base64().encode(cipher.doFinal()));
                            } catch (Exception ex) {
                                log.error("failed to encrypt", ex);
                                Notification.show("Error occured", ex.getClass().getSimpleName() + " " + ex.getMessage(),
                                        Notification.Type.TRAY_NOTIFICATION);
                            }
                        })
                        .build()
        );


        encryptLayout.addComponent(VaadinBuilders.textArea()
                        .setAttributes(vaWidth100, vaStyleMonospace)
                        .setHeight(8, Unit.EM)
                        .setCaption("Cypher Text")
                        .setPropertyDataSource(encryptedText)
                        .build()
        );

        encryptLayout.addComponent(VaadinBuilders.button()
                        .setCaption("Decrypt")
                        .addClickListener(e -> {
                            try {
                                Security.addProvider(new BouncyCastleProvider());
                                PEMReader pemReader = new PEMReader(new StringReader(publicPem.getValue()));
                                PublicKey keyPair = (PublicKey) pemReader.readObject();
                                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                                cipher.init(Cipher.DECRYPT_MODE, keyPair);
                                cipher.update(BaseEncoding.base64().decode(encryptedText.getValue()));
                                decryptedText.setValue(new String(cipher.doFinal()));
                            } catch (Exception ex) {
                                Notification.show("Error occured", ex.getClass().getSimpleName() + " " + ex.getMessage(),
                                        Notification.Type.TRAY_NOTIFICATION);
                            }
                        })
                        .build()
        );

        encryptLayout.addComponent(VaadinBuilders.textField()
                        .setAttributes(vaWidth100, vaReadOnly)
                        .setCaption("Decrypted Text")
                        .setPropertyDataSource(decryptedText)
                        .build()
        );

        return VaadinBuilders.horizontalLayout()
                .setAttributes(vaSizeFull, vaMargin, vaSpacing)
                .addComponent(VaadinBuilders.panel()
                        .setAttributes(vaHeight100)
                        .setCaption("Keys")
                        .setContent(keysLayout)
                        .build())
                .addComponent(VaadinBuilders.panel()
                        .setAttributes(vaHeight100)
                        .setCaption("Encrypt")
                        .setContent(encryptLayout)
                        .build())
                .build();
    }

    protected Component createChartLayout() {
        VerticalLayout chartLayout = VaadinBuilders.verticalLayout()
                .setAttributes(vaMargin, vaSpacing, vaWidth100, vaHeight100)
                .build();

        HighChart highChart = new HighChart();
        highChart.setOptionsJson("");
        highChart.setWidth(100.f, Unit.PERCENTAGE);
        highChart.setHeight(100.f, Unit.PERCENTAGE);

        HorizontalLayout cl = VaadinBuilders.horizontalLayout()
                .setAttributes(vaMargin, vaSpacing, vaWidth100, vaHeight100)
                .build();
        cl.addComponent(highChart);

        chartLayout.addComponent(cl);

        chartLayout.addComponent(
                VaadinBuilders.button()
                        .setCaption("update plot")
                        .setHeightUndefined()
                        .addClickListener(e -> {
                            Notification.show("update", Notification.Type.TRAY_NOTIFICATION);
                            highChart.setOptionsJson(hcOptions);
                        })
                        .build()
        );

        chartLayout.setExpandRatio(
                cl, 1.f
        );


        return chartLayout;
    }

    static ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(
        4, new ThreadFactoryBuilder().setDaemon(true).build());

    public static class Bean {
        ColorStyle colorStyle;
        String text;
        boolean value;

        public Bean(ColorStyle colorStyle) {
            this.colorStyle = colorStyle;
            this.text = colorStyle.name();
        }

        public boolean isValue() {
            return value;
        }

        public void setValue(boolean value) {
            this.value = value;
        }
    }

    static class UpdateSubscribers {
        List<Runnable> subscribers = new ArrayList<>();
        void add(Runnable r) {
            subscribers.add(r);
        }
        void fire() {
            subscribers.forEach(Runnable::run);
        }
    }

    Property<Boolean> checkBoxValue = new ObjectProperty<>(true);

    enum Columns {
        date,
        time,
        number1,
        text1,
        text2,
        text3,
        number2,
        number3,
        link
    }

    Component createTableTab() {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(Columns.date, String.class, null);
        container.addContainerProperty(Columns.time, String.class, null);
        container.addContainerProperty(Columns.number1, Integer.class, null);
        container.addContainerProperty(Columns.text1, String.class, null);
        container.addContainerProperty(Columns.text2, String.class, null);
        container.addContainerProperty(Columns.text3, String.class, null);
        container.addContainerProperty(Columns.number2, Integer.class, null);
        container.addContainerProperty(Columns.number3, Integer.class, null);
        container.addContainerProperty(Columns.link, String.class, null);


        for (int i = 0; i < 10000; ++i) {
            Item item = container.addItem(i);
            item.getItemProperty(Columns.date).setValue("date " + i);
            item.getItemProperty(Columns.time).setValue("time " + i);
            item.getItemProperty(Columns.number1).setValue(i * 100);
            item.getItemProperty(Columns.text1).setValue("value value1 " + i);
            item.getItemProperty(Columns.text2).setValue("value value2 " + i);
            item.getItemProperty(Columns.text3).setValue("value value3 " + i);
            item.getItemProperty(Columns.number2).setValue(i);
            item.getItemProperty(Columns.number3).setValue(i);
            item.getItemProperty(Columns.link).setValue("link");
        }

        return VaadinBuilders.table()
            .setContainerDataSource(container)
            .setAttributes(vaWidth100, vaHeight100)
            .build();
    }


    enum FormChoice {
        Good, Bad, Ugly
    }

    protected Component createVaadinFormLayout() {
        AtomicInteger updateCount = new AtomicInteger();
        List<FormProperty<?>> properties = ImmutableList.of(
            FormProperty.button("Button"),
            FormProperty.stringTextField("Text", "Text Field", "initial"),
            FormProperty.stringTextField("Text CW", "Text Field Custom Width", "1")
                .setCustomComponentModifiers(
                    VaadinForm.FormLayoutType.Horizontal,
                    c -> c.setWidth(40.f, Unit.PIXELS)
                ),

            FormProperty.integerTextField("Integer", "Integer Number", 10),
            FormProperty.doubleTextField("Double", "Double Number", 10),
            FormProperty.checkBox("Boolean", "Boolean Flag", true),
            FormProperty.dateField("Date", "Date Value", DateTime.now()),
            FormProperty.label("Lists Section"),
            FormProperty.stringComboBox("String List", "String List Field", "A", ImmutableList.of("A","B","C")),
            FormProperty.comboBox(
                "Number List",
                "Number List Field",
                1,
                ImmutableList.of(1,2,3),
                ImmutableMap.of(
                    1, "value 1", 2, "value 2", 3, "value 3"
                ),
                Integer.class
            ),
            FormProperty.enumComboBox(
                "Choice List",
                "Choice List Field",
                FormChoice.Good
            ),
            FormProperty.comboBox(
                "Custom List",
                "Custom List With Names",
                FormChoice.Good,
                Arrays.asList(FormChoice.values()),
                ImmutableMap.of(
                    FormChoice.Good, "Very Good",
                    FormChoice.Bad, "Very Bad",
                    FormChoice.Ugly, "Very Ugly"
                ),
                FormChoice.class
            )
        );

        VaadinForm vaadinForm = new VaadinForm(properties);
        vaadinForm.addChangeListener(uip -> {
            String caption =  "Update #" + updateCount.incrementAndGet() + ": '" + uip.getLongName()
                + "' from " + uip.getValue()
                + " to " + uip.getProperty().getValue();
            Notification notif = new Notification(caption, Notification.Type.HUMANIZED_MESSAGE);
            notif.setDelayMsec(1500);
            notif.show(UI.getCurrent().getPage());
            uip.store();
        });

        return VaadinBuilders.verticalLayout()
            .setAttributes(vaMargin)
            .addComponent(VaadinBuilders.panel()
                .setAttributes(vaStyleMarginTiny)
                .setCaption("Horizontal")
                .setContent(vaadinForm.createHorizontalLayout(
                    vaStylePaddingTiny
                ))
                .build())
            .addComponent(VaadinBuilders.panel()
                .setAttributes(vaStyleMarginTiny)
                .setCaption("Vertical")
                .setContent(vaadinForm.createVerticalLayout(
                    vaStylePaddingTiny
                ))
                .build())
            .addComponent(VaadinBuilders.panel()
                .setAttributes(vaStyleMarginTiny)
                .setCaption("Form")
                .setContent(vaadinForm.createFormLayout())
                .build())
            .build();
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        TabSheet tabSheet = new TabSheet();
        tabSheet.setHeight(100.f, Unit.PERCENTAGE);
        tabSheet.addTab(createChartLayout(), "Chart");
        tabSheet.addTab(createVaTestLayout(), "Test");
        tabSheet.addTab(createCryptoLayout(), "Crypto");
        tabSheet.addTab(createTableTab(), "Table");
        tabSheet.addTab(createVaadinFormLayout(), "Forms");
        tabSheet.setSelectedTab(tabSheet.getTab(0));
        setContent(tabSheet);
    }

    @Override
    public void detach() {
        super.detach();
    }

    private static final String hcOptions = "" +
            "{\n" +
            "        title: {\n" +
            "            text: 'Monthly Average Temperature',\n" +
            "            x: -20 //center\n" +
            "        },\n" +
            "        subtitle: {\n" +
            "            text: 'Source: WorldClimate.com',\n" +
            "            x: -20\n" +
            "        },\n" +
            "        xAxis: {\n" +
            "            categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',\n" +
            "                'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']\n" +
            "        },\n" +
            "        yAxis: {\n" +
            "            title: {\n" +
            "                text: 'Temperature (°C)'\n" +
            "            },\n" +
            "            plotLines: [{\n" +
            "                value: 0,\n" +
            "                width: 1,\n" +
            "                color: '#808080'\n" +
            "            }]\n" +
            "        },\n" +
            "        tooltip: {\n" +
            "            valueSuffix: '°C'\n" +
            "        },\n" +
            "        legend: {\n" +
            "            layout: 'vertical',\n" +
            "            align: 'right',\n" +
            "            verticalAlign: 'middle',\n" +
            "            borderWidth: 0\n" +
            "        },\n" +
            "        series: [{\n" +
            "            name: 'Tokyo',\n" +
            "            data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]\n" +
            "        }, {\n" +
            "            name: 'New York',\n" +
            "            data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5]\n" +
            "        }, {\n" +
            "            name: 'Berlin',\n" +
            "            data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]\n" +
            "        }, {\n" +
            "            name: 'London',\n" +
            "            data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]\n" +
            "        }]\n" +
            "}\n";
}

