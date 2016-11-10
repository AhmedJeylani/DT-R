Response.Listener<String> responseList = new Response.Listener<String>() {
            /*
            this method happens when the response has been executed
            the response is given to us as "String response" from the register.php
             */
            @Override
            public void onResponse(String response) {
                //try and catch because the return may not be in the form of JSON String
                try {
                    //we encoded it into a JSON string (check Register.php)
                    JSONObject jsonRes = new JSONObject(response);
                    boolean successRes = jsonRes.getBoolean("success");
                    if(successRes) {
                        //Intent(the activity we are in, the activity we want to open)
                        Intent intent = new Intent(NewDetailsActivity.this,LoginActivity.class);
                        NewDetailsActivity.this.startActivity(intent);
                        Toast.makeText(NewDetailsActivity.this,"correct",Toast.LENGTH_SHORT).show();
                        //Toast.makeText(NewDetailsActivity.this,unStr +" "+ cityStr + " " + passInt,Toast.LENGTH_LONG).show();

                    }
                    else {
                        Toast.makeText(NewDetailsActivity.this,"Incorrect",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(NewDetailsActivity.this,"Incorrect",Toast.LENGTH_SHORT).show();
                }
            }
        };
        RegisterReq registerReq = new RegisterReq(unStr,cityStr,passInt,responseList);
        RequestQueue queue = Volley.newRequestQueue(NewDetailsActivity.this);
        queue.add(registerReq);