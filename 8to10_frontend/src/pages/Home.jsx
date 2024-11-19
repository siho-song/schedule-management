import FullcalendarView from "../components/home/FullcalendarView.jsx";
import PrivateHeader from "../components/PrivateHeader.jsx";

import "@/styles/home/Fullcalendar.css";

function Home() {
    return (
        <div>
            <PrivateHeader />
            <FullcalendarView />
        </div>
    );
}

export default Home;