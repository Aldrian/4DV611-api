package se.travappar.api.utils.publish.mailchimp;

import com.ecwid.mailchimp.MailChimpAPIVersion;
import com.ecwid.mailchimp.MailChimpMethod;
import com.ecwid.mailchimp.method.v2_0.lists.Email;

import java.util.List;

@MailChimpMethod.Method(name = "lists/static-segment-members-add", version = MailChimpAPIVersion.v2_0)
public class AddSegmentMemberMethod extends MailChimpMethod<AddSegmentMemberResult> {
    @Field
    public String id;
    @Field
    public int seg_id;
    @Field
    public List<Email> batch;
}
