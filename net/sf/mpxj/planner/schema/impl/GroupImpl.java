//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.21 at 11:46:13 GMT 
//


package net.sf.mpxj.planner.schema.impl;

public class GroupImpl implements net.sf.mpxj.planner.schema.Group, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, net.sf.mpxj.planner.schema.impl.runtime.UnmarshallableObject, net.sf.mpxj.planner.schema.impl.runtime.XMLSerializable, net.sf.mpxj.planner.schema.impl.runtime.ValidatableObject
{

    protected java.lang.String _AdminPhone;
    protected java.lang.String _AdminName;
    protected java.lang.String _AdminEmail;
    protected java.lang.String _Name;
    protected java.lang.String _Id;
    public final static java.lang.Class version = (net.sf.mpxj.planner.schema.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (net.sf.mpxj.planner.schema.Group.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "group";
    }

    public java.lang.String getAdminPhone() {
        return _AdminPhone;
    }

    public void setAdminPhone(java.lang.String value) {
        _AdminPhone = value;
    }

    public java.lang.String getAdminName() {
        return _AdminName;
    }

    public void setAdminName(java.lang.String value) {
        _AdminName = value;
    }

    public java.lang.String getAdminEmail() {
        return _AdminEmail;
    }

    public void setAdminEmail(java.lang.String value) {
        _AdminEmail = value;
    }

    public java.lang.String getName() {
        return _Name;
    }

    public void setName(java.lang.String value) {
        _Name = value;
    }

    public java.lang.String getId() {
        return _Id;
    }

    public void setId(java.lang.String value) {
        _Id = value;
    }

    public net.sf.mpxj.planner.schema.impl.runtime.UnmarshallingEventHandler createUnmarshaller(net.sf.mpxj.planner.schema.impl.runtime.UnmarshallingContext context) {
        return new net.sf.mpxj.planner.schema.impl.GroupImpl.Unmarshaller(context);
    }

    public void serializeBody(net.sf.mpxj.planner.schema.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("", "group");
        context.endNamespaceDecls();
        if (_AdminEmail!= null) {
            context.startAttribute("", "admin-email");
            try {
                context.text(((java.lang.String) _AdminEmail), "AdminEmail");
            } catch (java.lang.Exception e) {
                net.sf.mpxj.planner.schema.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        if (_AdminName!= null) {
            context.startAttribute("", "admin-name");
            try {
                context.text(((java.lang.String) _AdminName), "AdminName");
            } catch (java.lang.Exception e) {
                net.sf.mpxj.planner.schema.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        context.startAttribute("", "name");
        try {
            context.text(((java.lang.String) _Name), "Name");
        } catch (java.lang.Exception e) {
            net.sf.mpxj.planner.schema.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endAttribute();
        if (_AdminPhone!= null) {
            context.startAttribute("", "admin-phone");
            try {
                context.text(((java.lang.String) _AdminPhone), "AdminPhone");
            } catch (java.lang.Exception e) {
                net.sf.mpxj.planner.schema.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        context.startAttribute("", "id");
        try {
            context.text(((java.lang.String) _Id), "Id");
        } catch (java.lang.Exception e) {
            net.sf.mpxj.planner.schema.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endAttribute();
        context.endAttributes();
        context.endElement();
    }

    public void serializeAttributes(net.sf.mpxj.planner.schema.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeURIs(net.sf.mpxj.planner.schema.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (net.sf.mpxj.planner.schema.Group.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\'com.sun.msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000"
+"\tnameClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv."
+"grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000"
+"\fcontentModelt\u0000 Lcom/sun/msv/grammar/Expression;xr\u0000\u001ecom.sun."
+"msv.grammar.Expression\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Lj"
+"ava/lang/Boolean;L\u0000\u000bexpandedExpq\u0000~\u0000\u0003xppp\u0000sr\u0000\u001fcom.sun.msv.gra"
+"mmar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.BinaryExp"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1q\u0000~\u0000\u0003L\u0000\u0004exp2q\u0000~\u0000\u0003xq\u0000~\u0000\u0004ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007pps"
+"q\u0000~\u0000\u0007ppsr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bpps"
+"r\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\t"
+"nameClassq\u0000~\u0000\u0001xq\u0000~\u0000\u0004sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005value"
+"xp\u0000psr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/r"
+"elaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/ms"
+"v/util/StringPair;xq\u0000~\u0000\u0004ppsr\u0000-com.sun.msv.datatype.xsd.Norma"
+"lizedStringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatype.xsd.Strin"
+"gType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.datatype.xsd"
+".BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.Co"
+"ncreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatype"
+"Impl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/String;L\u0000\btypeNa"
+"meq\u0000~\u0000\u001cL\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceP"
+"rocessor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0010normalizedS"
+"tringsr\u00004com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Replac"
+"e\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0001sr\u00000com.sun.msv.grammar.Expression$NullSetExpr"
+"ession\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ej"
+"B\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001cL\u0000\fnamespaceURIq\u0000~\u0000\u001cxpt\u0000\u0005CDATAt\u0000\u0000sr\u0000"
+"#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq"
+"\u0000~\u0000\u001cL\u0000\fnamespaceURIq\u0000~\u0000\u001cxr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u000badmin-emailq\u0000~\u0000)sr\u00000com.sun.msv.grammar.Express"
+"ion$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000\u0011\u0001q\u0000~\u0000/sq\u0000~\u0000\rppsq"
+"\u0000~\u0000\u000fq\u0000~\u0000\u0012pq\u0000~\u0000\u0016sq\u0000~\u0000*t\u0000\nadmin-nameq\u0000~\u0000)q\u0000~\u0000/sq\u0000~\u0000\u000fppq\u0000~\u0000\u0016sq\u0000"
+"~\u0000*t\u0000\u0004nameq\u0000~\u0000)sq\u0000~\u0000\rppsq\u0000~\u0000\u000fq\u0000~\u0000\u0012pq\u0000~\u0000\u0016sq\u0000~\u0000*t\u0000\u000badmin-phone"
+"q\u0000~\u0000)q\u0000~\u0000/sq\u0000~\u0000\u000fppq\u0000~\u0000\u0016sq\u0000~\u0000*t\u0000\u0002idq\u0000~\u0000)sq\u0000~\u0000*t\u0000\u0005groupq\u0000~\u0000)sr"
+"\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000"
+"/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.su"
+"n.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000"
+"\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPoo"
+"l;xp\u0000\u0000\u0000\u0007\u0001pq\u0000~\u00008q\u0000~\u0000\fq\u0000~\u00001q\u0000~\u0000\nq\u0000~\u0000\u000bq\u0000~\u0000\tq\u0000~\u0000\u000ex"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends net.sf.mpxj.planner.schema.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(net.sf.mpxj.planner.schema.impl.runtime.UnmarshallingContext context) {
            super(context, "------------------");
        }

        protected Unmarshaller(net.sf.mpxj.planner.schema.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return net.sf.mpxj.planner.schema.impl.GroupImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  10 :
                        attIdx = context.getAttribute("", "admin-phone");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 13;
                            continue outer;
                        }
                        state = 13;
                        continue outer;
                    case  13 :
                        attIdx = context.getAttribute("", "id");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 16;
                            continue outer;
                        }
                        break;
                    case  0 :
                        if (("group" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        break;
                    case  4 :
                        attIdx = context.getAttribute("", "admin-name");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText3(v);
                            state = 7;
                            continue outer;
                        }
                        state = 7;
                        continue outer;
                    case  17 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  1 :
                        attIdx = context.getAttribute("", "admin-email");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText4(v);
                            state = 4;
                            continue outer;
                        }
                        state = 4;
                        continue outer;
                    case  7 :
                        attIdx = context.getAttribute("", "name");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText5(v);
                            state = 10;
                            continue outer;
                        }
                        break;
                }
                super.enterElement(___uri, ___local, ___qname, __atts);
                break;
            }
        }

        private void eatText1(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _AdminPhone = com.sun.xml.bind.WhiteSpaceProcessor.replace(value);
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Id = com.sun.xml.bind.WhiteSpaceProcessor.replace(value);
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _AdminName = com.sun.xml.bind.WhiteSpaceProcessor.replace(value);
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _AdminEmail = com.sun.xml.bind.WhiteSpaceProcessor.replace(value);
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText5(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Name = com.sun.xml.bind.WhiteSpaceProcessor.replace(value);
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  10 :
                        attIdx = context.getAttribute("", "admin-phone");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 13;
                            continue outer;
                        }
                        state = 13;
                        continue outer;
                    case  13 :
                        attIdx = context.getAttribute("", "id");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 16;
                            continue outer;
                        }
                        break;
                    case  4 :
                        attIdx = context.getAttribute("", "admin-name");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText3(v);
                            state = 7;
                            continue outer;
                        }
                        state = 7;
                        continue outer;
                    case  17 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  1 :
                        attIdx = context.getAttribute("", "admin-email");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText4(v);
                            state = 4;
                            continue outer;
                        }
                        state = 4;
                        continue outer;
                    case  16 :
                        if (("group" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 17;
                            return ;
                        }
                        break;
                    case  7 :
                        attIdx = context.getAttribute("", "name");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText5(v);
                            state = 10;
                            continue outer;
                        }
                        break;
                }
                super.leaveElement(___uri, ___local, ___qname);
                break;
            }
        }

        public void enterAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  10 :
                        if (("admin-phone" == ___local)&&("" == ___uri)) {
                            state = 11;
                            return ;
                        }
                        state = 13;
                        continue outer;
                    case  13 :
                        if (("id" == ___local)&&("" == ___uri)) {
                            state = 14;
                            return ;
                        }
                        break;
                    case  4 :
                        if (("admin-name" == ___local)&&("" == ___uri)) {
                            state = 5;
                            return ;
                        }
                        state = 7;
                        continue outer;
                    case  17 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  1 :
                        if (("admin-email" == ___local)&&("" == ___uri)) {
                            state = 2;
                            return ;
                        }
                        state = 4;
                        continue outer;
                    case  7 :
                        if (("name" == ___local)&&("" == ___uri)) {
                            state = 8;
                            return ;
                        }
                        break;
                }
                super.enterAttribute(___uri, ___local, ___qname);
                break;
            }
        }

        public void leaveAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  10 :
                        attIdx = context.getAttribute("", "admin-phone");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 13;
                            continue outer;
                        }
                        state = 13;
                        continue outer;
                    case  13 :
                        attIdx = context.getAttribute("", "id");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 16;
                            continue outer;
                        }
                        break;
                    case  6 :
                        if (("admin-name" == ___local)&&("" == ___uri)) {
                            state = 7;
                            return ;
                        }
                        break;
                    case  9 :
                        if (("name" == ___local)&&("" == ___uri)) {
                            state = 10;
                            return ;
                        }
                        break;
                    case  4 :
                        attIdx = context.getAttribute("", "admin-name");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText3(v);
                            state = 7;
                            continue outer;
                        }
                        state = 7;
                        continue outer;
                    case  12 :
                        if (("admin-phone" == ___local)&&("" == ___uri)) {
                            state = 13;
                            return ;
                        }
                        break;
                    case  17 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  3 :
                        if (("admin-email" == ___local)&&("" == ___uri)) {
                            state = 4;
                            return ;
                        }
                        break;
                    case  1 :
                        attIdx = context.getAttribute("", "admin-email");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText4(v);
                            state = 4;
                            continue outer;
                        }
                        state = 4;
                        continue outer;
                    case  7 :
                        attIdx = context.getAttribute("", "name");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText5(v);
                            state = 10;
                            continue outer;
                        }
                        break;
                    case  15 :
                        if (("id" == ___local)&&("" == ___uri)) {
                            state = 16;
                            return ;
                        }
                        break;
                }
                super.leaveAttribute(___uri, ___local, ___qname);
                break;
            }
        }

        public void handleText(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                try {
                    switch (state) {
                        case  10 :
                            attIdx = context.getAttribute("", "admin-phone");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText1(v);
                                state = 13;
                                continue outer;
                            }
                            state = 13;
                            continue outer;
                        case  13 :
                            attIdx = context.getAttribute("", "id");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText2(v);
                                state = 16;
                                continue outer;
                            }
                            break;
                        case  14 :
                            eatText2(value);
                            state = 15;
                            return ;
                        case  4 :
                            attIdx = context.getAttribute("", "admin-name");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText3(v);
                                state = 7;
                                continue outer;
                            }
                            state = 7;
                            continue outer;
                        case  8 :
                            eatText5(value);
                            state = 9;
                            return ;
                        case  17 :
                            revertToParentFromText(value);
                            return ;
                        case  11 :
                            eatText1(value);
                            state = 12;
                            return ;
                        case  5 :
                            eatText3(value);
                            state = 6;
                            return ;
                        case  1 :
                            attIdx = context.getAttribute("", "admin-email");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText4(v);
                                state = 4;
                                continue outer;
                            }
                            state = 4;
                            continue outer;
                        case  2 :
                            eatText4(value);
                            state = 3;
                            return ;
                        case  7 :
                            attIdx = context.getAttribute("", "name");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText5(v);
                                state = 10;
                                continue outer;
                            }
                            break;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

    }

}
