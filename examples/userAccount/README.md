A _user_account_ table, where:

* the data must have _user_id_, _email_address_ and _creation_date_ fields
* _user_id_ is a non-optional string
* _email_address_ must be populated and contain a @ character
  * however, if _user_id_ is itself an email address, _email_address_ must be absent
* _creation_date_ is a non-optional date, with no time component, later than 2003 and output per [ISO 8601](https://en.wikipedia.org/wiki/ISO_8601)
