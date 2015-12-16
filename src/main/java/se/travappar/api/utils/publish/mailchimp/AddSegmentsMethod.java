package se.travappar.api.utils.publish.mailchimp;

import com.ecwid.mailchimp.MailChimpAPIVersion;
import com.ecwid.mailchimp.MailChimpMethod;

@MailChimpMethod.Method(name = "lists/static-segment-add", version = MailChimpAPIVersion.v2_0)
public class AddSegmentsMethod extends MailChimpMethod<AddSegmentsResult> {
    @Field
    public String id;
    @Field
    public String name;
}
