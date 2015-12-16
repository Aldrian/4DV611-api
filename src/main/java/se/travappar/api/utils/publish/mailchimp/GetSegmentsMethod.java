package se.travappar.api.utils.publish.mailchimp;

import com.ecwid.mailchimp.MailChimpAPIVersion;
import com.ecwid.mailchimp.MailChimpMethod;

@MailChimpMethod.Method(name = "lists/segments", version = MailChimpAPIVersion.v2_0)
public class GetSegmentsMethod extends MailChimpMethod<GetSegmentsResult> {
    @Field
    public String id;
    @Field
    public String type;
}
