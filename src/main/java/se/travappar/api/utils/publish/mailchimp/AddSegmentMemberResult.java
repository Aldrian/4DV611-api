package se.travappar.api.utils.publish.mailchimp;

import com.ecwid.mailchimp.MailChimpObject;

public class AddSegmentMemberResult extends MailChimpObject {

    @Field
    public Integer success_count;//	the total number of successful updates (will include members already in the segment)

    @Field
    public Integer error_count;//	the total number of errors
    //array	errors;//	structs for each error including:
}
