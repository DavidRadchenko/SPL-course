package bgu.spl171.net.impl.packet;

/**
 * Created by david on 1/13/17.
 */
public class LOGRQ extends Packet {

        private String userName;


        public LOGRQ(String filename) {
            setOpcode((short) 7);
            this.userName = filename;
        }

        public String getFilename() {
            return userName;
        }

    }

