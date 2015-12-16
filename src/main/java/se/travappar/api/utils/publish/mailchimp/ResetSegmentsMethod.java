package se.travappar.api.utils.publish.mailchimp;

import com.ecwid.mailchimp.MailChimpAPIVersion;
import com.ecwid.mailchimp.MailChimpMethod;

@MailChimpMethod.Method(name = "lists/static-segment-reset", version = MailChimpAPIVersion.v2_0)
public class ResetSegmentsMethod extends MailChimpMethod<ResetSegmentResult> {
    @Field
    public String id;
    @Field
    public int seg_id;
}
