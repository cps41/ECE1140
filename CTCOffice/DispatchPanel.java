//There will be panels for each partiton of CTC functions.
//They will be instantiated in the RailLinePanel constuctor
//They are customized first by the RailLinePanel.setDestinations(Stringx x) function
//The RailLinePanel.setDestinations(String x).setDestinatinos(String x) function then calls the
//DispatchPanel.setDestinations(String x) function to fill the JComboBox, Destintation_ComboBox









/*abstract


import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
    ///private JTextField AUTHORITY_TEXTFIELD = new JTextField("Authority",6);
    //private JSlider SPEED_SLIDER = new JSlider(JSlider.HORIZONTAL,0,70,0);
    //private JLabel SPEED_SLIDER_LABEL = new JLabel("Speed");
    //private JLabel SPEED_SELECTION_LABEL = new JLabel("35 KPH");

 private boolean checkAuthorityFormat(String authority){
        String message = "Authority is out of Range. Range is 0 to "+CONTROLLER.NUMBER_0F_BLOCKS+".";
        try{ Integer.parseInt(authority);} catch (Exception e){
            errorMessage(message);
            return false;
        }
        int authorityInt = Integer.parseInt(authority);
        if (authorityInt > CONTROLLER.NUMBER_0F_BLOCKS || authorityInt < 0 ){
            errorMessage(message);
            return false;
        }
        return true;
    }

    private void configureSlider(){
        SPEED_SLIDER.setPaintTicks(true);
        SPEED_SLIDER.setMajorTickSpacing(10);
        SPEED_SLIDER.setPaintLabels(true);
        SPEED_SLIDER.setPaintTrack(true);
        class SliderListener implements ChangeListener {
            @Override
            public void stateChanged(ChangeEvent e){
                SPEED_SELECTION_LABEL.setText(SPEED_SLIDER.getValue()+" KPH");
            }
        }
        SPEED_SLIDER.addChangeListener(new SliderListener());
        add(SPEED_SLIDER);
    }


*/