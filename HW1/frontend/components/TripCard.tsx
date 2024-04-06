import { Trip } from "@/types/trip";
import {
  Card,
  CardHeader,
  CardBody,
  CardFooter,
  Divider,
} from "@nextui-org/react";
import { MaterialSymbol } from "react-material-symbols";

export default function TripCard({ trip }: { trip: Trip }) {
  return (
    <Card
      key={trip.id}
      isPressable={trip.freeSeats > 0}
      isDisabled={trip.freeSeats === 0}
      onPress={() => console.log(trip.id)}
    >
      <CardHeader className="flex flex-col justify-start items-start">
        <p className="text-lg font-bold">
          {trip.departureTime.toLocaleString("en-US", {
            year: "numeric",
            month: "long",
            day: "numeric",
            hour: "numeric",
            minute: "numeric",
          })}
        </p>
        <p className="text-small text-default-500">
          Arrival at{" "}
          {trip.arrivalTime.toLocaleString("en-US", {
            year: "numeric",
            month: "long",
            day: "numeric",
            hour: "numeric",
            minute: "numeric",
          })}
        </p>
      </CardHeader>
      <Divider />
      <CardBody>
        <div className="grid grid-cols-2">
          <div className="flex flex-row gap-2 items-center">
            <MaterialSymbol icon="flight_takeoff" size={20} />
            <p>{trip.departure.name}</p>
          </div>
          <div className="flex flex-row gap-2 items-center">
            <MaterialSymbol icon="payments" size={20} />
            <p>
              {trip.price.toLocaleString("pt-PT", {
                style: "currency",
                currency: "EUR",
              })}
            </p>
          </div>
          <div className="flex flex-row gap-2 items-center">
            <MaterialSymbol icon="flight_land" size={20} />
            <p>{trip.arrival.name}</p>
          </div>
          <div className="flex flex-row gap-2 items-center">
            <MaterialSymbol icon="directions_bus" size={20} />
            <p>{trip.bus.capacity}-seat bus</p>
          </div>
        </div>
      </CardBody>
      <Divider />
      <CardFooter>
        {trip.freeSeats === 0 ? "No" : trip.freeSeats} seats available
      </CardFooter>
    </Card>
  );
}